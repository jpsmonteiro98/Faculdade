import matplotlib.pyplot as plt
import numpy as np
import psycopg2


conn = psycopg2.connect("dbname='trab' user='arcaic' host='localhost' password='password'")
cursor_psql = conn.cursor()
sql = "select dw_taxi.taxi, dw_state.estado, sum(total_metros) as total_metros, sum(total_trajectos), sum(total_tempo) as tempo from dw_facts_table2, dw_taxi, dw_state, dw_tempo where dw_facts_table2.taxi_id=dw_taxi.id and dw_facts_table2.estado_id=dw_state.estado_id and dw_facts_table2.tempo_id = dw_tempo.tempo_id and estado = 'BUSY' group by rollup(1,2) order by 4 desc limit 15"
#sql = "select st_astext(proj_location) from taxi_stands,tracks where st_distance(st_startpoint(proj_track), proj_location) < 1500000 and name = 'Campanhã'"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
taxi = []
estado = []
n_distancia = []
n_metros=[]
tempo= []
count =0
for row in results:
    if row[0] not in taxi:
        taxi.append(row[0])
        estado.append(row[1])
        n_metros.append(row[2]/1000)
        n_distancia.append(row[3])
        tempo.append(row[4]/60)

total=n_distancia[0]

n_metros.remove(n_metros[0])
taxi.remove(taxi[0])
estado.remove(estado[0])
n_distancia.remove(n_distancia[0])
tempo.remove(tempo[0])


x = np.arange(len(taxi))  # the label locations
width = 0.3  # the width of the bars

fig, ax = plt.subplots()
rects1 = ax.bar(x - width/2, n_distancia, width, label='Numero_trajetos')
rects2 = ax.bar(x + width/2 , n_metros, width, label='Total_metros (Km)')

# Add some text for labels, title and custom x-axis tick labels, etc.
ax.set_ylabel('Valores')
ax.set_title('(AVEIRO) Top 7 taxis com maior número de trajetos no estado BUSY (número total de trajetos estado BUSY = ' + str(total) + ')' )
ax.set_xticks(x)
ax.set_xticklabels(taxi)
ax.legend()

ax.bar_label(rects1, padding=3)

fig.tight_layout()

plt.show()

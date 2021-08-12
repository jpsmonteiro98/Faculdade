import matplotlib.pyplot as plt
import numpy as np
import psycopg2

list_distritos=[]
count = 0
conn = psycopg2.connect("dbname='trab' user='arcaic' host='localhost' password='password'")
cursor_psql = conn.cursor()
sql = "select dw_all_taxi.taxi, dw_tempo.hora, sum(dw_facts_table3.total_metros) as total_metros, sum(total_trajectos) as total_trajetos from dw_facts_table3, dw_all_taxi, dw_state, dw_tempo  where dw_facts_table3.taxi_id=dw_all_taxi.id and dw_facts_table3.estado_id=dw_state.estado_id and dw_facts_table3.tempo_id=dw_tempo.tempo_id and dw_all_taxi.taxi like '20014%' group by 1,2 order by 1,2 desc"
#sql = "select st_astext(proj_location) from taxi_stands,tracks where st_distance(st_startpoint(proj_track), proj_location) < 1500000 and name = 'Campanhã'"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()

if count == 0:
    taxi = []
    hora = []
    n_metros=[]


    for row in results:
        taxi.append(row[0])
        hora.append(row[1])
        n_metros.append(row[2]/1000)

    lista =[]
    lista_metros =[]
    lista_tempo = []

    lista.append(taxi)
    lista.append(hora)
    lista.append(n_metros)

    cont_metros = lista[2][0]
    tempo_max = lista[1][0]
    tempo_min = 0
    total_tempo=0
    lista_km= []

    for i in range(len(lista[0])):


        if i == 0:
            tempo_max=lista[1][i]
            count_metros = lista[2][i]
        else:

            if(lista[0][i]==lista[0][i-1]):
                cont_metros += lista[2][i]

            else:
                   tempo_min = lista[1][i-1]

                   total_tempo = tempo_max-tempo_min
                   if total_tempo == 0:
                       total_tempo = 1

                   lista_tempo.append(tempo_max-tempo_min)
                   tempo_max = lista[1][i]
                   lista_metros.append(cont_metros)

                   cont_metros = lista[2][i]

        if(i==len(lista[0])-1):
            if(lista[0][i]==lista[0][i-1]):
                lista_metros.append(cont_metros)
                tempo_min = lista[1][i]
                total_tempo = tempo_max-tempo_min
                if total_tempo == 0:
                    total_tempo = 1
                lista_tempo.append(total_tempo)
            else:

                lista_metros.append(lista[2][i])
                lista_tempo.append(1)

    for i in range(len(lista_metros)):
        lista_km.append(lista_metros[i]/lista_tempo[i])


    list_distritos.append(max(lista_km))

    count =1

sql = "select dw_all_taxi.taxi, dw_tempo.hora, sum(dw_facts_table3.total_metros) as total_metros, sum(total_trajectos) as total_trajetos from dw_facts_table3, dw_all_taxi, dw_state, dw_tempo  where dw_facts_table3.taxi_id=dw_all_taxi.id and dw_facts_table3.estado_id=dw_state.estado_id and dw_facts_table3.tempo_id=dw_tempo.tempo_id and dw_all_taxi.taxi like '20004%' group by 1,2 order by 1,2 desc"
#sql = "select st_astext(proj_location) from taxi_stands,tracks where st_distance(st_startpoint(proj_track), proj_location) < 1500000 and name = 'Campanhã'"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()


if count == 1:
    taxi = []
    hora = []
    n_metros=[]


    for row in results:
        taxi.append(row[0])
        hora.append(row[1])
        n_metros.append(row[2]/1000)

    lista =[]
    lista_metros =[]
    lista_tempo = []

    lista.append(taxi)
    lista.append(hora)
    lista.append(n_metros)

    cont_metros = lista[2][0]
    tempo_max = lista[1][0]
    tempo_min = 0
    total_tempo=0
    lista_km= []

    for i in range(len(lista[0])):


        if i == 0:
            tempo_max=lista[1][i]
            count_metros = lista[2][i]
        else:

            if(lista[0][i]==lista[0][i-1]):
                cont_metros += lista[2][i]

            else:
                   tempo_min = lista[1][i-1]

                   total_tempo = tempo_max-tempo_min
                   if total_tempo == 0:
                       total_tempo = 1

                   lista_tempo.append(tempo_max-tempo_min)
                   tempo_max = lista[1][i]
                   lista_metros.append(cont_metros)

                   cont_metros = lista[2][i]

        if(i==len(lista[0])-1):
            if(lista[0][i]==lista[0][i-1]):
                lista_metros.append(cont_metros)
                tempo_min = lista[1][i]
                total_tempo = tempo_max-tempo_min
                if total_tempo == 0:
                    total_tempo = 1
                lista_tempo.append(total_tempo)
            else:

                lista_metros.append(lista[2][i])
                lista_tempo.append(1)


    for i in range(len(lista_metros)):
        if(lista_tempo[i] !=0):
            lista_km.append(lista_metros[i]/lista_tempo[i])
    list_distritos.append(max(lista_km))

    count =2


sql = "select dw_all_taxi.taxi, dw_tempo.hora, sum(dw_facts_table3.total_metros) as total_metros, sum(total_trajectos) as total_trajetos from dw_facts_table3, dw_all_taxi, dw_state, dw_tempo  where dw_facts_table3.taxi_id=dw_all_taxi.id and dw_facts_table3.estado_id=dw_state.estado_id and dw_facts_table3.tempo_id=dw_tempo.tempo_id and dw_all_taxi.taxi like '20007%' group by 1,2 order by 1,2 desc"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()

if count == 2:

    taxi = []
    hora = []
    n_metros=[]


    for row in results:
        taxi.append(row[0])
        hora.append(row[1])
        n_metros.append(row[2]/1000)

    lista =[]
    lista_metros =[]
    lista_tempo = []

    lista.append(taxi)
    lista.append(hora)
    lista.append(n_metros)

    cont_metros = lista[2][0]
    tempo_max = lista[1][0]
    tempo_min = 0
    total_tempo=0
    lista_km= []

    for i in range(len(lista[0])):


        if i == 0:
            tempo_max=lista[1][i]
            count_metros = lista[2][i]
        else:

            if(lista[0][i]==lista[0][i-1]):
                cont_metros += lista[2][i]

            else:
                   tempo_min = lista[1][i-1]

                   total_tempo = tempo_max-tempo_min
                   if total_tempo == 0:
                       total_tempo = 1

                   lista_tempo.append(tempo_max-tempo_min)
                   tempo_max = lista[1][i]
                   lista_metros.append(cont_metros)

                   cont_metros = lista[2][i]

        if(i==len(lista[0])-1):
            if(lista[0][i]==lista[0][i-1]):
                lista_metros.append(cont_metros)
                tempo_min = lista[1][i]
                total_tempo = tempo_max-tempo_min
                if total_tempo == 0:
                    total_tempo = 1
                lista_tempo.append(total_tempo)
            else:

                lista_metros.append(lista[2][i])
                lista_tempo.append(1)


    for i in range(len(lista_metros)):
        if(lista_tempo[i] !=0):
            lista_km.append(lista_metros[i]/lista_tempo[i])
    list_distritos.append(max(lista_km))

    count =3



sql = "select dw_all_taxi.taxi, dw_tempo.hora, sum(dw_facts_table3.total_metros) as total_metros, sum(total_trajectos) as total_trajetos from dw_facts_table3, dw_all_taxi, dw_state, dw_tempo  where dw_facts_table3.taxi_id=dw_all_taxi.id and dw_facts_table3.estado_id=dw_state.estado_id and dw_facts_table3.tempo_id=dw_tempo.tempo_id and dw_all_taxi.taxi like '20008%' group by 1,2 order by 1,2 desc"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()

if count == 3:

    taxi = []
    hora = []
    n_metros=[]


    for row in results:
        taxi.append(row[0])
        hora.append(row[1])
        n_metros.append(row[2]/1000)

    lista =[]
    lista_metros =[]
    lista_tempo = []

    lista.append(taxi)
    lista.append(hora)
    lista.append(n_metros)

    cont_metros = lista[2][0]
    tempo_max = lista[1][0]
    tempo_min = 0
    total_tempo=0
    lista_km= []

    for i in range(len(lista[0])):


        if i == 0:
            tempo_max=lista[1][i]
            count_metros = lista[2][i]
        else:

            if(lista[0][i]==lista[0][i-1]):
                cont_metros += lista[2][i]

            else:
                   tempo_min = lista[1][i-1]

                   total_tempo = tempo_max-tempo_min
                   if total_tempo == 0:
                       total_tempo = 1

                   lista_tempo.append(tempo_max-tempo_min)
                   tempo_max = lista[1][i]
                   lista_metros.append(cont_metros)

                   cont_metros = lista[2][i]

        if(i==len(lista[0])-1):
            if(lista[0][i]==lista[0][i-1]):
                lista_metros.append(cont_metros)
                tempo_min = lista[1][i]
                total_tempo = tempo_max-tempo_min
                if total_tempo == 0:
                    total_tempo = 1
                lista_tempo.append(total_tempo)
            else:

                lista_metros.append(lista[2][i])
                lista_tempo.append(1)


    for i in range(len(lista_metros)):
        if(lista_tempo[i] !=0):
            lista_km.append(lista_metros[i]/lista_tempo[i])
    list_distritos.append(max(lista_km))

    count =4


sql = "select dw_all_taxi.taxi, dw_tempo.hora, sum(dw_facts_table3.total_metros) as total_metros, sum(total_trajectos) as total_trajetos from dw_facts_table3, dw_all_taxi, dw_state, dw_tempo  where dw_facts_table3.taxi_id=dw_all_taxi.id and dw_facts_table3.estado_id=dw_state.estado_id and dw_facts_table3.tempo_id=dw_tempo.tempo_id and dw_all_taxi.taxi like '20000%' group by 1,2 order by 1,2 desc"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()


if count == 4:

    taxi = []
    hora = []
    n_metros=[]


    for row in results:
        taxi.append(row[0])
        hora.append(row[1])
        n_metros.append(row[2]/1000)

    lista =[]
    lista_metros =[]
    lista_tempo = []

    lista.append(taxi)
    lista.append(hora)
    lista.append(n_metros)

    cont_metros = lista[2][0]
    tempo_max = lista[1][0]
    tempo_min = 0
    total_tempo=0
    lista_km= []

    for i in range(len(lista[0])):


        if i == 0:
            tempo_max=lista[1][i]
            count_metros = lista[2][i]
        else:

            if(lista[0][i]==lista[0][i-1]):
                cont_metros += lista[2][i]

            else:
                   tempo_min = lista[1][i-1]

                   total_tempo = tempo_max-tempo_min
                   if total_tempo == 0:
                       total_tempo = 1

                   lista_tempo.append(tempo_max-tempo_min)
                   tempo_max = lista[1][i]
                   lista_metros.append(cont_metros)

                   cont_metros = lista[2][i]

        if(i==len(lista[0])-1):
            if(lista[0][i]==lista[0][i-1]):
                lista_metros.append(cont_metros)
                tempo_min = lista[1][i]
                total_tempo = tempo_max-tempo_min
                if total_tempo == 0:
                    total_tempo = 1
                lista_tempo.append(total_tempo)
            else:

                lista_metros.append(lista[2][i])
                lista_tempo.append(1)


    for i in range(len(lista_metros)):
        if(lista_tempo[i] !=0):
            lista_km.append(lista_metros[i]/lista_tempo[i])
    list_distritos.append(max(lista_km))


plt.rcdefaults()
fig, ax = plt.subplots()

# Example data
objects = ('Aveiro', 'Lisboa', 'Braga', 'Coimbra', 'Porto')
y_pos = np.arange(len(objects))
performance = []
for i in range(len(list_distritos)):
    performance.append(list_distritos[i])

plt.barh(y_pos, performance, align='center', alpha=0.5)
plt.yticks(y_pos, objects)
plt.xlabel('km/h')
plt.title('Veículo com maior velocidade media durante o dia por distrito ')

plt.show()

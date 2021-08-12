import math

import matplotlib.pyplot as plt
import psycopg2


def polygon_to_points(poly_string):
    xarr = []
    yarr = []
    points = poly_string[9:-2].split(',')
    for point in points:
        (x, y) = point.split()
        xarr.append(float(x))
        yarr.append(float(y))
    return xarr, yarr


scale = 1 / 30000
conn = psycopg2.connect("dbname='trab' user='arcaic' host='localhost' password='password'")
cursor_psql = conn.cursor()


sql = "select dw_all_taxi.taxi, dw_state.estado, sum(dw_facts_table3.total_metros) as total_metros, " \
      "dw_facts_table3.tempo_id from dw_facts_table3, dw_all_taxi, dw_state  where " \
      "dw_facts_table3.taxi_id=dw_all_taxi.id and dw_facts_table3.estado_id=dw_state.estado_id and (dw_state.estado = " \
      "'BUSY' OR dw_state.estado='PICKUP') group by 1,2,4 order by 1,3 desc; "
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
dic = {}
for row in results:
    receita = 0
    if 6 <= row[3] <= 20:
        if row[1] == 'BUSY':
            receita = (math.ceil(row[2]/1000) * 0.47) + 3.25
        elif row[1] == 'PICKUP':
            receita = (math.ceil(row[2]/1000) * 0.94) + 3.25
    else:
        if row[1] == 'BUSY':
            receita = (math.ceil(row[2]/1000) * 0.56) + 3.90
        elif row[1] == 'PICKUP':
            receita = (math.ceil(row[2]/1000) * 1.13) + 3.90

    if dic.get(row[0]) is None:
        dic.update({row[0]: receita})
    else:
        dic.update({row[0]: dic.get(row[0]) + receita})

# sot dictionary by values
dic = {key: val for key, val in sorted(dic.items(), key = lambda ele: ele[1], reverse = True)}

t = dic.keys()
taxis = []
for key in t:
    taxis.append(key)

for i in range(len(taxis)):
    sql = "select dw_all_taxi.taxi, sum(dw_facts_table3.total_metros) as total_metros, sum(total_trajectos) as " \
          "total_trajetos from dw_facts_table3, dw_all_taxi  where dw_facts_table3.taxi_id=dw_all_taxi.id and taxi='" \
          + taxis[i] + "' group by 1 order by 1,3 desc; "
    cursor_psql.execute(sql)
    results = cursor_psql.fetchall()

    for row in results:
        dic.update({taxis[i]: dic.get(taxis[i]) - (row[1]/1000 * 1.403 * 0.18)})

dic = {key: val for key, val in sorted(dic.items(), key = lambda ele: ele[1], reverse = True)}

taxis = []
t = dic.keys()
for key in t:
    taxis.append(key)

final = {}
hours = {}

for i in range(10):
    sql = "select dw_all_taxi.taxi, dw_state.estado, sum(dw_facts_table3.total_metros) as total_metros, " \
      "dw_facts_table3.tempo_id from dw_facts_table3, dw_all_taxi, dw_state  where " \
      "dw_facts_table3.taxi_id=dw_all_taxi.id and dw_facts_table3.estado_id=dw_state.estado_id and dw_all_taxi.taxi = " \
      "'" + taxis[i] + "' group by 4,2,1 order by 4 asc; "

    cursor_psql.execute(sql)
    results = cursor_psql.fetchall()
    for row in results:
        if 6 <= row[3] <= 20:
            if row[1] == 'FREE':
                if hours.get(row[3]) is None:
                    hours.update({row[3]: - round(row[2]/1000) * 1.403 * 0.18})
                else:
                    hours.update({row[3]: hours.get(row[3]) - round(row[2]/1000) * 1.403 * 0.18})
            elif row[1] == 'BUSY':
                if hours.get(row[3]) is None:
                    hours.update({row[3]: - (round(row[2]/1000) * 1.403 * 0.18) + (math.ceil(row[2]/1000) * 0.47) + 3.25})
                else:
                    hours.update({row[3]: hours.get(row[3]) - (round(row[2]/1000) * 1.403 * 0.18) + (math.ceil(row[2]/1000) * 0.47) + 3.25})
            elif row[1] == 'PICKUP':
                if hours.get(row[3]) is None:
                    hours.update({row[3]: - (round(row[2]/1000) * 1.403 * 0.18) + (math.ceil(row[2]/1000) * 0.47) + 3.25})
                else:
                    hours.update({row[3]: hours.get(row[3]) - (round(row[2]/1000) * 1.403 * 0.18) + (math.ceil(row[2]/1000) * 0.47) + 3.25})
            elif row[1] == 'PAUSE':
                if hours.get(row[3]) is None:
                    hours.update({row[3]: - round(row[2]/1000) * 1.403 * 0.18})
                else:
                    hours.update({row[3]: hours.get(row[3]) - round(row[2]/1000) * 1.403 * 0.18})
        else:
            if row[1] == 'FREE':
                if hours.get(row[3]) is None:
                    hours.update({row[3]: - round(row[2]/1000) * 1.403 * 0.18})
                else:
                    hours.update({row[3]: hours.get(row[3]) - round(row[2]/1000) * 1.403 * 0.18})
            elif row[1] == 'BUSY':
                if hours.get(row[3]) is None:
                    hours.update({row[3]: - (round(row[2]/1000) * 1.403 * 0.18) + (math.ceil(row[2]/1000) * 0.56) + 3.90})
                else:
                    hours.update({row[3]: hours.get(row[3]) - (round(row[2]/1000) * 1.403 * 0.18) + (math.ceil(row[2]/1000) * 0.56) + 3.90})
            elif row[1] == 'PICKUP':
                if hours.get(row[3]) is None:
                    hours.update({row[3]: - (round(row[2]/1000) * 1.403 * 0.18) + (math.ceil(row[2]/1000) * 0.56) + 3.90})
                else:
                    hours.update({row[3]: hours.get(row[3]) - (round(row[2]/1000) * 1.403 * 0.18) + (math.ceil(row[2]/1000) * 0.56) + 3.90})
            elif row[1] == 'PAUSE':
                if hours.get(row[3]) is None:
                    hours.update({row[3]: - round(row[2]/1000) * 1.403 * 0.18})
                else:
                    hours.update({row[3]: hours.get(row[3]) - round(row[2]/1000) * 1.403 * 0.18})

    final.update({taxis[i]: hours})
    hours = {}


hour = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]

dic = {key: val for key, val in sorted(dic.items(), key=lambda ele: ele[1], reverse=True)}

for i in range(10):
    profit = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]

    for j in range(24):
        if final[taxis[i]].get(j) is not None:
            if j != 0:
                profit[j] = profit[j-1] + final[taxis[i]].get(j)
            else:
                profit[j] = final[taxis[i]].get(j)
        else:
            if j != 0:
                profit[j] = profit[j-1]
            else:
                profit[j] = 0
    plt.plot(hour,profit, '.-')


plt.grid(True)
plt.legend([taxis[0], taxis[1],  taxis[2],  taxis[3], taxis[4], taxis[5], taxis[6], taxis[7], taxis[8], taxis[9]])
plt.xlim(0, 23)
plt.xticks([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23])

plt.title('Top 10 Most Profitable Taxis', fontdict={'fontsize': 22})
plt.xlabel('Hour', fontdict={'fontsize': 18})
plt.ylabel('Profit (€)', fontdict={'fontsize': 18})
plt.show()

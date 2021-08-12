import matplotlib as mpl
import matplotlib.cm
import matplotlib.pyplot as plt
import psycopg2
from matplotlib.colors import to_rgb


def polygon_to_points(polygon_string):
    xs, ys = [],[]
    points = polygon_string[9:-2].split(',')
    for point in points:
        (x,y) = point.split()
        xs.append(float(x))
        ys.append(float(y))
    return xs,ys


scale=1/30000
conn = psycopg2.connect("dbname='trab' user='arcaic' host='localhost' password='password'")
cursor_psql = conn.cursor()
# Calculate figure size
sql ="select st_astext(st_envelope(st_collect(st_simplify(proj_boundary,100,FALSE)))) from cont_aad_caop2018 where concelho='PORTO'"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
row = results[0]
polygon_string = row[0]
xs,ys = polygon_to_points(polygon_string)
width_in_inches = ((max(xs)-min(xs))/0.0254)*1.1
height_in_inches = ((max(ys)-min(ys))/0.0254)*1.1
fig = plt.figure(figsize=(width_in_inches*scale,height_in_inches*scale))

sql = "select st_astext(st_simplify(proj_boundary,10,False)) from cont_aad_caop2018 where distrito in ('PORTO')"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
flag = 0
vector = [0, 0.5, 1]
xxs, yys = [], []
for row in results:
    polygon_string = row[0]
    xs, ys = polygon_to_points(polygon_string)
    plt.plot(xs,ys,color='black')




sql = "select st_astext(st_simplify(proj_boundary,10,False)), count (id) as total from cont_aad_caop2018, tracks where st_intersects(proj_boundary, st_endpoint(proj_track)) and distrito= 'PORTO' and state='BUSY' group by 1;"

cursor_psql.execute(sql)
results = cursor_psql.fetchall()
total= []
xx, yy = [], []
cmap = matplotlib.cm.get_cmap('jet')
for row in results:
    total.append(row[1])
    polygon_string = row[0]
    xs, ys = polygon_to_points(polygon_string)
    xx.append(xs)
    yy.append(ys)


def rbg_to_hex(rbg):
    return '%02x%02x%02x' % rbg


max = max(total)
for i in range(len(total)):
    rbga = cmap(total[i] / max)
    x = xx[i]
    y = yy[i]
    plt.plot(x, y, color='k')
    rbg = to_rgb(rbga)
    plt.fill(x, y, color=rbg)

norm = mpl.colors.Normalize(vmin=0, vmax=max)
plt.title('Taxi Destinations around Porto',  fontdict={'fontsize': 22})
plt.colorbar(mpl.cm.ScalarMappable(norm=norm, cmap=cmap))
plt.show()

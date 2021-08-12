import matplotlib.pyplot as plt
import psycopg2
from postgis import Polygon,MultiPolygon
from postgis.psycopg import register
from matplotlib.animation import FuncAnimation


def animate(i):
    scat.set_offsets([x[i],y[i]])


def linestring_to_points(line_string):
    xs, ys = [],[]
    points = line_string[11:-1].split(',')
    for point in points:
        (x,y) = point.split()
        xs.append(float(x))
        ys.append(float(y))
    return xs,ys


def polygon_to_points2(collection):
    xys = collection[0].coords
    for (x,y) in xys:
        xs.append(float(x))
        ys.append(float(y))
    return xs, ys


def multipolygon_to_points(multipolygon_string):
    xs, ys = [], []
    for pol in multipolygon_string:
        xys = pol[0].coords
        for (x,y) in xys:
            xs.append(float(x))
            ys.append(float(y))
        plt.plot(xs, ys, color='black', lw='0.2')
        xs, ys = [], []
    return xs, ys


def linestring_to_points2(line_string):
    xs, ys = [], []
    points = line_string[11:-1].split(',')
    xprev, yprev, x, y = float(0), float(0), float(0), float(0)
    for point in points:
        (x, y) = point.split()
        xs.append(float(x))
        ys.append(float(y))
    plt.plot(xs, ys, color='lightgray', lw=0.2, )


scale=1/60000
conn = psycopg2.connect("dbname='trab' user='arcaic' host='localhost' password='password'")
register(conn)
cursor_psql = conn.cursor()

xs_min, xs_max, ys_min, ys_max = -50000, -30000, 160000, 172000
width_in_inches = (xs_max-xs_min)/0.0254*1.1
height_in_inches = (ys_max-ys_min)/0.0254*1.1
fig, ax = plt.subplots(figsize=(width_in_inches*scale, height_in_inches*scale))
ax.set(xlim=(xs_min, xs_max), ylim=(ys_min, ys_max))



sql = "select distrito,st_union(st_simplify(proj_boundary, 10, False)) from cont_aad_caop2018 group by distrito"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
xs , ys = [],[]
for row in results:
    collection = row[1]
    if type(collection) is MultiPolygon:
        xs, ys = multipolygon_to_points(collection)
    elif type(collection) is Polygon:
        xs, ys = polygon_to_points2(collection)
        plt.plot(xs, ys, color='black', lw='0.2')
    xs, ys = [], []

sql = "select st_astext(proj_track), id, taxi, ST_Length(proj_track) from tracks where state='BUSY' order by 4 desc limit 5000;"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
xs, ys = [], []
dic = {}
line_string = ""
for row in results:
    line_string = row[0]
    linestring_to_points2(line_string)


sql = "select id, taxi, ST_Length(proj_track) from tracks where state='BUSY' order by 3 desc limit 1;"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
taxi = 0
id = 0
for row in results:
    id = row[0]
    taxi = row[1]

sql = "select st_astext(proj_track) from tracks where taxi='"+ str(taxi) + "' and id ='" + str(id) +  "' order by ts"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
x, y = [], []
for row in results:
    xs, ys = linestring_to_points(row[0])
    x = x + xs
    y = y + ys
scat = ax.scatter(x[0],y[0],s=15, color='red')
print(len(y))
anim = FuncAnimation(fig, animate, interval=10, frames=len(y)-1)
anim.save('filename.mp4')
plt.draw()
plt.show()
conn.close()

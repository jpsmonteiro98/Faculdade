import matplotlib.pyplot as plt
import psycopg2


def polygon_to_points(polygon_string):
    xs = []
    ys = []
    points = polygon_string[9:-2].split(',')
    for point in points:
        (x, y) = point.split()
        xs.append(float(x))
        ys.append(float(y))
    return xs, ys


def point_to_points(point_string):
    xs = []
    ys = []
    points = point_string[6:-1].split(',')
    for point in points:
        (x, y) = point.split()
        xs.append(float(x))
        ys.append(float(y))
    return xs, ys


def linestring_to_points(line_string):
    xs = []
    ys = []
    points = line_string[11:-2].split(',')
    for point in points:
        (x, y) = point.split()
        xs.append(float(x))
        ys.append(float(y))
    return xs, ys


scale = 1/30000
conn = psycopg2.connect("dbname='trab' user='arcaic' host='localhost' password='password'")
cursor_psql = conn.cursor()

sql = "select st_astext(st_envelope(st_collect(st_simplify(proj_boundary, 100, FALSE)))) from cont_aad_caop2018 where concelho='PORTO'"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
row = results[0]
polygon_string = row[0]
xs, ys = polygon_to_points(polygon_string)
width_in_inches = ((max(xs)-min(xs))/0.0254)*1.1
height_in_inches = ((max(ys)-min(ys))/0.0254)*1.1
fig = plt.figure(figsize=(width_in_inches*scale,height_in_inches*scale))


sql = "select st_astext(st_simplify(proj_boundary,10,False)) from cont_aad_caop2018 where concelho in ('PORTO');"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
for row in results:
    polygon_string = row[0]
    xs, ys = polygon_to_points(polygon_string)
    plt.plot(xs,ys, color='black')

xs = []
ys = []

sql = "select name, count(tracks.id), st_astext(proj_location) from tracks , taxi_stands  where st_dwithin(proj_location, st_startpoint(proj_track), 50)  group by name, proj_location;"
cursor_psql.execute(sql)
results = cursor_psql.fetchall()
val = []
for row in results:
    val.append(row[1])
    point_string = row[2]
    x, y = point_to_points(point_string)
    xs.append(float(x[0]))
    ys.append(float(y[0]))
plt.scatter(xs, ys, s=val, c=val, cmap='jet')
plt.colorbar()

plt.title('Tracks initiated in taxi stands ', fontdict={'fontsize': 22})

plt.show()
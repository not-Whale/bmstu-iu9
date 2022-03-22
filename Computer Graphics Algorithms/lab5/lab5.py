import pyglet

from math import sqrt
from pyglet import app
from pyglet.gl import *
from pyglet.window import mouse


window = pyglet.window.Window(700, 700, caption="Lab5")

window.set_minimum_size(200, 200)
window.set_maximum_size(1000, 1000)

screen_width, screen_height = 500, 500

glClearColor(0, 0, 0, 0)

mode = GL_LINE_LOOP

stage = 1
EPS = 1E-9

Intersections_OUT = []
figure1_vertex = []
figure2_vertex = []
figure1 = []
figure2 = []
figure_current = []
Result_Figures = []

prohibited = False
inside_case = False
way_in = True
way_out = False


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.begin = False
        self.used = False
        self.way_in = False
        self.way_out = False

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def print(self):
        print("x =", self.x, "y =", self.y,
              "begin =", self.begin, "used =", self.used,
              "in =", self.way_in, "out =", self.way_out)


class pt:
    def __init__(self):
        self.x = 0
        self.y = 0

    def __lt__(self, other):
        return self.x < other.x - EPS or (abs(self.x - other.x) < EPS and self.y < other.y - EPS)


class Line:
    def __init__(self, p, q):
        self.a = p.y - q.y
        self.b = q.x - p.x
        self.c = -self.a * p.x - self.b * p.y
        self.norm()

    def norm(self):
        global EPS
        z = sqrt(self.a ** 2 + self.b ** 2)
        if abs(z) > EPS:
            self.a /= z
            self.b /= z
            self.c /= z

    def dist(self, p):
        return self.a * p.x + self.b * p.y + self.c


work = Point(0, 0)


@window.event()
def on_draw():
    global mode, screen_width, screen_height
    global Result_Figures, Intersections_OUT
    global inside_case, prohibited
    global figure1_vertex, figure2_vertex
    window.clear()

    glClearColor(0, 0, 0, 0)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    glViewport(0, 0, screen_width, screen_height)
    # glOrtho(0, screen_width, 0, screen_height, -1, 1)

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()

    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()

    for x in Result_Figures:
        draw_line(x, 1, 1, 1)
        draw_points(x, 1, 1, 1)

    if inside_case:
        draw_line(figure1_vertex, 0, 1, 0)
        draw_line(figure2_vertex, 0, 1, 0)
        draw_points(figure1_vertex, 1, 1, 1)
        draw_points(figure2_vertex, 1, 1, 1)

    draw_points(Intersections_OUT, 1, 1, 1)

    if not prohibited:
        draw_line(figure1_vertex, 1, 0, 0)
        draw_line(figure2_vertex, 0, 0, 1)


@window.event()
def on_resize(width, height):
    global screen_width, screen_height
    screen_width = width
    screen_height = height
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    glViewport(0, 0, screen_width, screen_height)
    window.clear()


@window.event()
def on_mouse_press(x, y, button, modifiers):
    if button & mouse.LEFT:
        if stage == 1:
            p = Point(x, y)
            p.begin = True
            figure1_vertex.append(p)
        elif stage == 2:
            p = Point(x, y)
            p.begin = True
            figure2_vertex.append(p)
        elif stage == 3:
            p = Point(x, y)
            p.begin = True
            print((2 * p.x - screen_width) / screen_width, (2 * p.y - screen_height) / screen_height)


@window.event()
def on_key_press(key, modifiers):
    global stage, mode, prohibited
    if key == pyglet.window.key.S:
        stage += 1
        if stage == 3:
            weiler_atherton()
    if key == pyglet.window.key.SPACE:
        if mode == GL_LINE_LOOP:
            mode = GL_POLYGON
        else:
            mode = GL_LINE_LOOP
    if key == pyglet.window.key.D:
        prohibited = not prohibited


def draw_line(figure_vertex, r, g, b):
    global screen_width, screen_height, mode
    glColor3f(r, g, b)
    glBegin(mode)
    for p in figure_vertex:
        glVertex2f((2 * p.x - screen_width) / screen_width, (2 * p.y - screen_height) / screen_height)
    glEnd()


def draw_points(figure_vertex, r, g, b):
    global screen_width, screen_height
    glColor3f(r, g, b)
    glPointSize(5)
    glBegin(GL_POINTS)
    for p in figure_vertex:
        glVertex2f((2 * p.x - screen_width) / screen_width, (2 * p.y - screen_height) / screen_height)
    glEnd()


def intersection(p1, p2, p3, p4):
    if p2.x < p1.x:
        p1, p2 = p2, p1

    if p4.x < p3.x:
        p3, p4 = p4, p3

    if p2.x < p3.x:
        return False

    if p1.x - p2.x == 0 and p3.x - p4.x == 0:
        if p1.x == p3.x:
            if not (max(p1.y, p2.y) < min(p3.y, p4.y)) or (min(p1.y, p2.y) > max(p3.y, p4.y)):
                return True
        return False

    if p1.x - p2.x == 0:
        Xa = p1.x
        a2 = (p3.y - p4.y) / (p3.x - p4.x)
        b2 = p3.y - a2 * p3.x
        Ya = a2 * Xa + b2
        if p3.x <= Xa <= p4.x and min(p1.y, p2.y) <= Ya <= max(p1.y, p2.y):
            return True
        return False

    if p3.x - p4.x == 0:
        Xa = p3.x
        a1 = (p1.y - p2.y) / (p1.x - p2.x)
        b1 = p1.y - a1 * p1.x
        Ya = a1 * Xa + b1
        if p1.x <= Xa <= p2.x and min(p3.y, p4.y) <= Ya <= max(p3.y, p4.y):
            return True
        return False

    a1 = (p1.y - p2.y) / (p1.x - p2.x)
    a2 = (p3.y - p4.y) / (p3.x - p4.x)
    b1 = p1.y - a1 * p1.x
    b2 = p3.y - a2 * p3.x
    if a1 == a2:
        return False

    Xa = (b2 - b1) / (a1 - a2)
    if min(p2.x, p4.x) < Xa < max(p1.x, p3.x):
        return False
    else:
        return True


def det(a, b, c, d):
    return a * d - b * c


def betw(l, r, x):
    global EPS
    return min(l, r) <= x + EPS and x <= max(l, r) + EPS


def intersect_1d(a, b, c, d):
    if a > b:
        a, b = b, a
    if c > d:
        c, d = d, c
    return max(a, c) <= min(b, d) + EPS


def intersect(a, b, c, d, left, right):
    if not intersect_1d(a.x, b.x, c.x, d.x) or not intersect_1d(a.y, b.y, c.y, d.y):
        return False

    m = Line(a, b)
    n = Line(c, d)
    zn = det(m.a, m.b, n.a, n.b)
    if abs(zn) < EPS:
        if abs(m.dist(c)) > EPS or abs(n.dist(a)) > EPS:
            return False
        if b < a:
            a, b = b, a
        if d < c:
            c, d = d, c
        left = max(a, c)
        right = min(b, d)
        return True
    else:
        left.x = right.x = -det(m.c, m.b, n.c, n.b) / zn
        left.y = right.y = -det(m.a, m.c, n.a, n.c) / zn
        return betw(a.x, b.x, left.x) and betw(a.y, b.y, left.y) and betw(c.x, d.x, left.x) and betw(c.y, d.y, left.y)


def put_in_order2(anchor, intersecs):
    global way_in, way_out, figure2
    while len(intersecs) > 0:
        min_dist = 9999999999
        pos = 0
        for (i, p) in enumerate(intersecs):
            dx = (anchor.x - p.x) ** 2
            dy = (anchor.y - p.y) ** 2
            dist = sqrt(dx + dy)
            if min_dist > dist:
                min_dist = dist
                pos = i
        intersecs[pos].way_in = way_in
        intersecs[pos].way_out = way_out

        way_in = not way_in
        way_out = not way_out

        figure2.append(intersecs[pos])
        intersecs.pop(pos)


def put_in_order1(anchor, intersecs):
    global way_in, way_out, Intersections_OUT, figure1
    while len(intersecs) > 0:
        min_dist = 9999999999
        pos = 0
        for (i, p) in enumerate(intersecs):
            dx = (anchor.x - p.x) ** 2
            dy = (anchor.y - p.y) ** 2
            dist = sqrt(dx + dy)
            if min_dist > dist:
                min_dist = dist
                pos = i
        intersecs[pos].way_in = way_in
        intersecs[pos].way_out = way_out

        way_in = not way_in
        way_out = not way_out

        if intersecs[pos].way_out:
            Intersections_OUT.append(intersecs[pos])

        figure1.append(intersecs[pos])
        intersecs.pop(pos)


def find_intersections():
    global way_in, way_out
    global figure1_vertex, figure2_vertex
    global figure1, figure2
    anchor = Point(0, 0)
    allintersec = []
    way_in = True
    way_out = False
    p11, p12, p21, p22 = pt(), pt(), pt(), pt()
    res1, res2 = pt(), pt()
    for (i, p) in enumerate(figure1_vertex):
        p11.x = figure1_vertex[i % len(figure1_vertex)].x
        p11.y = figure1_vertex[i % len(figure1_vertex)].y

        p12.x = figure1_vertex[(i + 1) % len(figure1_vertex)].x
        p12.y = figure1_vertex[(i + 1) % len(figure1_vertex)].y

        anchor = figure1_vertex[i % len(figure1_vertex)]

        if i == 0:
            figure1.append(figure1_vertex[i % len(figure1_vertex)])

        for (j, p2) in enumerate(figure2_vertex):
            p21.x = figure2_vertex[j % len(figure2_vertex)].x
            p21.y = figure2_vertex[j % len(figure2_vertex)].y

            p22.x = figure2_vertex[(j + 1) % len(figure2_vertex)].x
            p22.y = figure2_vertex[(j + 1) % len(figure2_vertex)].y

            if intersect(p11, p12, p21, p22, res1, res2):
                allintersec.append(Point(res1.x, res1.y))

        put_in_order1(anchor, allintersec)
        allintersec.clear()
        figure1.append(figure1_vertex[(i + 1) % len(figure1_vertex)])

    allintersec.clear()

    for (i, p) in enumerate(figure2_vertex):
        p11.x = figure2_vertex[i % len(figure2_vertex)].x
        p11.y = figure2_vertex[i % len(figure2_vertex)].y

        p12.x = figure2_vertex[(i + 1) % len(figure2_vertex)].x
        p12.y = figure2_vertex[(i + 1) % len(figure2_vertex)].y

        anchor = figure2_vertex[i % len(figure2_vertex)]

        if i == 0:
            figure2.append(figure2_vertex[i % len(figure2_vertex)])

        for (j, p2) in enumerate(figure1_vertex):
            p21.x = figure1_vertex[j % len(figure1_vertex)].x
            p21.y = figure1_vertex[j % len(figure1_vertex)].y

            p22.x = figure1_vertex[(j + 1) % len(figure1_vertex)].x
            p22.y = figure1_vertex[(j + 1) % len(figure1_vertex)].y

            if intersect(p11, p12, p21, p22, res1, res2):
                allintersec.append(Point(res1.x, res1.y))

        put_in_order2(anchor, allintersec)
        allintersec.clear()
        figure2.append(figure2_vertex[(i + 1) % len(figure2_vertex)])


def search(array, find):
    for (i, x) in enumerate(array):
        if x == find:
            return i
    return -1


def check_stack(find):
    global Intersections_OUT
    pos = search(Intersections_OUT, find)
    if pos != -1:
        Intersections_OUT[pos].used = True


def find_1(cur):
    global figure1, figure_current, Result_Figures
    pos = search(figure1, cur)
    figure_current.append(figure1[pos % len(figure1)])
    pos += 1
    jump = False
    while not jump:
        if figure1[pos % len(figure1)].begin:
            figure_current.append(figure1[pos % len(figure1)])
        else:
            figure1[pos % len(figure1)].used = True
            check_stack(figure1[pos % len(figure1)])
            find_2(figure1[pos % len(figure1)])
            jump = True
        pos += 1


def find_2(cur):
    global figure2, figure_current, work, Result_Figures
    pos = search(figure2, cur)
    figure_current.append(figure2[pos % len(figure2)])
    pos -= 1
    jump = False
    while not jump:
        if figure2[pos % len(figure2)].begin:
            figure_current.append(figure2[pos % len(figure2)])
        if figure2[pos % len(figure2)] == work and (not figure2[pos % len(figure2)].used):
            figure_current.append(figure2[pos % len(figure2)])
            jump = True
            check_stack(figure2[pos % len(figure2)])
            figure2[pos % len(figure2)].used = True
            Result_Figures.append(figure_current)
            figure_current.clear()
        if (not figure2[pos % len(figure2)] == work) and (figure2[pos % len(figure2)].way_in
                                                          or figure2[pos % len(figure2)].way_out):
            jump = True
            check_stack(figure2[pos % len(figure2)])
            figure2[pos % len(figure2)].used = True
            find_1(figure2[pos % len(figure2)])
        pos -= 1
    jump = False
    pos = 0


def check_stack_extra_points():
    global figure1, figure2, Intersections_OUT
    for (i, p1) in enumerate(figure1):
        if figure1[i].begin:
            for (j, p2) in enumerate(Intersections_OUT):
                if figure1[i] == Intersections_OUT[j]:
                    Intersections_OUT.pop(j)
    for (i, p1) in enumerate(figure2):
        if figure2[i].begin:
            for (j, p2) in enumerate(Intersections_OUT):
                if figure2[i] == Intersections_OUT[j]:
                    Intersections_OUT.pop(j)


def inside(p, figure):
    count = 0
    for (i, pf) in enumerate(figure):
        p1 = figure[i % len(figure)]
        p2 = figure[(i + 1) % len(figure)]
        if intersection(Point(0, 0), Point(p.x, p.y), Point(p1.x, p1.y), Point(p2.x, p2.y)):
            count += 1
    if count % 2 == 0:
        return False
    else:
        return True


def weiler_atherton():
    global Intersections_OUT, figure1, figure2
    global figure1_vertex, figure2_vertex
    global inside_case, work
    find_intersections()
    # print_vector(Intersections_OUT, "Пересечение")
    # print_vector(figure1, "Фигура 1")
    # print_vector(figure2, "Фигура 2")
    if len(Intersections_OUT) == 0:
        if inside(figure2_vertex[0], figure1_vertex):
            inside_case = True
            return
        if not inside(figure2_vertex[0], figure1_vertex):
            Result_Figures.append(figure1_vertex)
            return
        if inside(figure1_vertex[0], figure2_vertex):
            return
    check_stack_extra_points()
    j = 0
    while j < len(Intersections_OUT):
        work = Intersections_OUT[j]
        if not work.used:
            find_1(work)
        j += 1


def print_vector(vector, message):
    print(message)
    for P in vector:
        P.print()


app.run()

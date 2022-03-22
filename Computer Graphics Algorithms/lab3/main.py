import pyglet
import math
from pyglet import app
from pyglet.gl import *

x, y, z = 0.0, 0.0, 0.0
rotateA, rotateB, rotateC = 0.0, 0.0, 0.0
rotateX, rotateY, rotateZ = 0.0, 0.0, 0.0
screen_width, screen_height = 500, 500
change = 0.5
scale = 1.0
levels = 20
wireframe_mode = True

window = pyglet.window.Window(500, 500, caption="Lab3")

window.set_minimum_size(200, 200)
window.set_maximum_size(1000, 1000)

# glEnable(GL_DEPTH_TEST)


def draw_pyramid():
    global wireframe_mode, levels
    global rotateA, rotateB, rotateC
    global rotateX, rotateY, rotateZ
    global x, y, z

    mode = GL_LINE_LOOP
    if wireframe_mode:
        mode = GL_POLYGON

    size = 60

    points = []

    for i in range(levels):
        buf = []
        for j in range(12):
            buf.append(0.0)
        points.append(buf)

    glTranslatef(x, y, z)
    glLineWidth(2.0)

    k = size/levels
    l = size

    rotateX = rotateA/levels
    rotateY = rotateB/levels

    for i in range(levels):
        points[i][0] = (x + -l) / 3 * math.cos(rotateY) + (z + -l) / 3 * math.sin(rotateY)
        points[i][1] = y + -l
        points[i][2] = -(x + -l) / 3 * math.sin(rotateY) + (z + -l) / 3 * math.cos(rotateY)

        points[i][3] = (x + -l) / 3 * math.cos(rotateY) + (z + l) / 3 * math.sin(rotateY)
        points[i][4] = y + -l
        points[i][5] = -(x + -l) / 3 * math.sin(rotateY) + (z + l) / 3 * math.cos(rotateY)

        points[i][6] = (x + l) / 3 * math.cos(rotateY) + (z + l) / 3 * math.sin(rotateY)
        points[i][7] = y + -l
        points[i][8] = -(x + l) / 3 * math.sin(rotateY) + (z + l) / 3 * math.cos(rotateY)

        points[i][9] = (x + l) / 3 * math.cos(rotateY) + (z + -l) / 3 * math.sin(rotateY)
        points[i][10] = y + -l
        points[i][11] = -(x + l) / 3 * math.sin(rotateY) + (z + -l) / 3 * math.cos(rotateY)

        l -= k
        rotateX = rotateX + rotateA / levels
        rotateY = rotateY + rotateB / levels

    glTranslatef(x, y, z)

    glBegin(mode)
    glColor3ub(139, 0, 0)
    glVertex3f(points[levels - 1][0], points[levels - 1][1], points[levels - 1][2])
    glColor3ub(255, 69, 0)
    glVertex3f(x / 2, y, z / 2)
    glColor3ub(240, 128, 128)
    glVertex3f(points[levels - 1][3], points[levels - 1][4], points[levels - 1][5])
    glEnd()

    glBegin(mode)
    glColor3ub(139, 0, 0)
    glVertex3f(x / 2, y, z / 2)
    glColor3ub(255, 69, 0)
    glVertex3f(points[levels - 1][6], points[levels - 1][7], points[levels - 1][8])
    glColor3ub(220, 20, 60)
    glVertex3f(points[levels - 1][9], points[levels - 1][10], points[levels - 1][11])
    glEnd()

    glBegin(mode)
    glColor3ub(139, 0, 0)
    glVertex3f(points[levels - 1][0], points[levels - 1][1], points[levels - 1][2])
    glColor3ub(240, 128, 128)
    glVertex3f(x / 2, y, z / 2)
    glColor3ub(220, 20, 60)
    glVertex3f(points[levels - 1][9], points[levels - 1][10], points[levels - 1][11])
    glEnd()

    glBegin(mode)
    glColor3ub(240, 128, 128)
    glVertex3f(points[levels - 1][3], points[levels - 1][4], points[levels - 1][5])
    glColor3ub(255, 69, 0)
    glVertex3f(x / 2, y, z / 2)
    glColor3ub(220, 20, 60)
    glVertex3f(points[levels - 1][9], points[levels - 1][10], points[levels - 1][11])
    glEnd()

    for i in range(levels):
        # bottom
        glBegin(mode)
        glColor3ub(139, 0, 0)
        glVertex3f(points[i - 1][0], points[i - 1][1], points[i - 1][2])
        glColor3ub(240, 128, 128)
        glVertex3f(points[i - 1][3], points[i - 1][4], points[i - 1][5])
        glColor3ub(255, 69, 0)
        glVertex3f(points[i - 1][6], points[i - 1][7], points[i - 1][8])
        glColor3ub(220, 20, 60)
        glVertex3f(points[i - 1][9], points[i - 1][10], points[i - 1][11])
        glEnd()

        # front
        glBegin(mode)
        glColor3ub(139, 0, 0)
        glVertex3f(points[i - 1][3], points[i - 1][4], points[i - 1][5])
        glColor3ub(240, 128, 128)
        glVertex3f(points[i][3], points[i][4], points[i][5])
        glColor3ub(255, 69, 0)
        glVertex3f(points[i][6], points[i][7], points[i][8])
        glColor3ub(220, 20, 60)
        glVertex3f(points[i - 1][6], points[i - 1][7], points[i - 1][8])
        glEnd()

        # left
        glBegin(mode)
        glColor3ub(139, 0, 0)
        glVertex3f(points[i - 1][0], points[i - 1][1], points[i - 1][2])
        glColor3ub(240, 128, 128)
        glVertex3f(points[i][0], points[i][1], points[i][2])
        glColor3ub(255, 69, 0)
        glVertex3f(points[i][3], points[i][4], points[i][5])
        glColor3ub(220, 20, 60)
        glVertex3f(points[i - 1][3], points[i - 1][4], points[i - 1][5])
        glEnd()

        # back
        glBegin(mode)
        glColor3ub(139, 0, 0)
        glVertex3f(points[i - 1][0], points[i - 1][1], points[i - 1][2])
        glColor3ub(240, 128, 128)
        glVertex3f(points[i][0], points[i][1], points[i][2])
        glColor3ub(255, 69, 0)
        glVertex3f(points[i][9], points[i][10], points[i][11])
        glColor3ub(220, 20, 60)
        glVertex3f(points[i - 1][9], points[i - 1][10], points[i - 1][11])
        glEnd()

        # right
        glBegin(mode)
        glColor3ub(139, 0, 0)
        glVertex3f(points[i - 1][6], points[i - 1][7], points[i - 1][8])
        glColor3ub(240, 128, 128)
        glVertex3f(points[i][6], points[i][7], points[i][8])
        glColor3ub(255, 69, 0)
        glVertex3f(points[i][9], points[i][10], points[i][11])
        glColor3ub(220, 20, 60)
        glVertex3f(points[i - 1][9], points[i - 1][10], points[i - 1][11])
        glEnd()

    glBegin(mode)
    glColor3ub(139, 0, 0)
    glVertex3f(points[levels - 1][0], points[levels - 1][1], points[levels - 1][2])
    glColor3ub(240, 128, 128)
    glVertex3f(points[levels - 1][3], points[levels - 1][4], points[levels - 1][5])
    glColor3ub(255, 69, 0)
    glVertex3f(points[levels - 1][6], points[levels - 1][7], points[levels - 1][8])
    glColor3ub(220, 20, 60)
    glVertex3f(points[levels - 1][9], points[levels - 1][10], points[levels - 1][11])
    glEnd()


'''def draw_cube():
    glColor3ub(205, 92, 92)
    glBegin(GL_QUADS)
    glVertex3f(10, -90, -90)
    glVertex3f(20, -90, -90)
    glVertex3f(20, -100, -90)
    glVertex3f(10, -100, -90)
    glEnd()

    glColor3ub(240, 128, 128)
    glBegin(GL_QUADS)
    glVertex3f(10, -90, -100)
    glVertex3f(20, -90, -100)
    glVertex3f(20, -100, -100)
    glVertex3f(10, -100, -100)
    glEnd()

    glColor3ub(220, 20, 60)
    glBegin(GL_QUADS)
    glVertex3f(10, -90, -90)
    glVertex3f(10, -90, -100)
    glVertex3f(10, -100, -100)
    glVertex3f(10, -100, -90)
    glEnd()

    glColor3ub(178, 34, 34)
    glBegin(GL_QUADS)
    glVertex3f(20, -90, -90)
    glVertex3f(20, -90, -100)
    glVertex3f(20, -100, -100)
    glVertex3f(20, -100, -90)
    glEnd()

    glColor3ub(255, 160, 122)
    glBegin(GL_QUADS)
    glVertex3f(10, -100, -90)
    glVertex3f(10, -100, -100)
    glVertex3f(20, -100, -100)
    glVertex3f(20, -100, -90)
    glEnd()

    glColor3ub(233, 150, 122)
    glBegin(GL_QUADS)
    glVertex3f(10, -90, -90)
    glVertex3f(10, -90, -100)
    glVertex3f(20, -90, -100)
    glVertex3f(20, -90, -90)
    glEnd()'''


@window.event()
def on_resize(width, height):
    global screen_width, screen_height
    screen_width, screen_height = width, height
    glViewport(0, 0, screen_width, screen_height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()


@window.event()
def on_draw():
    global scale, x, y, z, rotateA, rotateB, rotateC

    window.clear()
    glClearColor(1.0, 1.0, 1.0, 1.0)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

    glViewport(0, 0, screen_width, screen_height)

    ratio = screen_width / screen_height
    z_near = 10
    z_far = 1000
    dy = z_near * math.tan(((45 / 2) * math.pi) / 180)
    dx = ratio * dy

    glPushMatrix()
    # draw_cube()
    glPopMatrix()

    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    glFrustum(-dx, dx, -dy, dy, z_near, z_far)
    glTranslatef(0, 0, -200)
    glRotatef(-45, 0, 1, 0)

    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()

    glPushMatrix()
    glScalef(scale, scale, scale)
    glTranslatef(x, y, z)
    glRotatef(rotateA, 1.0, 0.0, 0.0)
    glRotatef(rotateB, 0.0, 1.0, 0.0)
    glTranslatef(-x, -y, -z)
    draw_pyramid()
    glPopMatrix()


@window.event()
def on_key_press(key, modifiers):
    global wireframe_mode, scale, x, y, z, levels
    global rotateA, rotateB, rotateC

    if key == pyglet.window.key.SPACE:
        wireframe_mode = not wireframe_mode
    if key == pyglet.window.key.BACKSPACE:
        rotateA, rotateB, rotateC = 0.0, 0.0, 0.0
        scale = 1.0
        x, y, z = 0.0, 0.0, 0.0
    if key == pyglet.window.key.Q and scale < 2:
        scale += 0.1
    if key == pyglet.window.key.E and scale > 0.1:
        scale -= 0.1
    if key == pyglet.window.key.NUM_ADD and levels <= 19:
        levels += 1
    if key == pyglet.window.key.NUM_SUBTRACT and levels >= 6:
        levels -= 1
    if key == pyglet.window.key.NUM_4:
        x -= 0.2
    if key == pyglet.window.key.NUM_6:
        x += 0.2
    if key == pyglet.window.key.NUM_8:
        y += 0.2
    if key == pyglet.window.key.NUM_2:
        y -= 0.2
    if key == pyglet.window.key.NUM_7:
        z += 0.2
    if key == pyglet.window.key.NUM_9:
        z -= 0.2
    if key == pyglet.window.key.W:
        rotateA -= 2.0
    if key == pyglet.window.key.A:
        rotateB -= 0.1
    if key == pyglet.window.key.S:
        rotateA += 2.0
    if key == pyglet.window.key.D:
        rotateB += 0.1


app.run()

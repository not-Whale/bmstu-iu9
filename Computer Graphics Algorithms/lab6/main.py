import pickle
import pyglet
import math
import os
from pyglet import app
from pyglet.gl import *

x, y, z = 0.0, 5, 0.0
rotateA, rotateB, rotateC = 0.0, 0.0, 0.0
rotateX, rotateY, rotateZ = 0.0, 0.0, 0.0
screen_width, screen_height = 500, 500
change = 0.5
scale = 1.0
levels = 20
wireframe_mode = True
animation_mode = False
texture_mode = False
spotlight_mode = False
first_b = False
up = False
pyramid_position = [0, 0, 0]
position = [0, 0, 0]
t, dt, t_start = 0, 0, 0
v, g = 200, 10
delta_b = 0
delta = 10
cutoff = 90
cutoff_delta = 20

window = pyglet.window.Window(500, 500, caption="Lab6")

window.set_minimum_size(200, 200)
window.set_maximum_size(1000, 1000)

glClearColor(0, 0, 0, 1)


# glEnable(GL_DEPTH_TEST)


def vector(a, b):
    mull = [-(a[1] * b[2] - b[1] * a[2]),
            -(b[0] * a[2] - a[0] * b[2]),
            -(a[0] * b[1] - b[0] * a[1])]
    return ort(mull, math.sqrt(mull[0] ** 2 + mull[1] ** 2 + mull[2] ** 2))


def sub(a, b):
    return [a[0] - b[0], a[1] - b[1], a[2] - b[2]]


def add(a, b):
    return [a[0] + b[0], a[1] + b[1], a[2] + b[2]]


def ort(a, b):
    return [a[0] / b, a[1] / b, a[2] / b]


def upd(delta):
    global t, dt
    dt = delta
    t += delta


def animate(dt):
    global v, g, t_start, delta_b, position, up
    if delta_b < -220:
        up = True
        v = -v
        t_start = t
    tb = t - t_start
    v -= g * dt
    if up:
        delta_b = -220 + tb * v - g * tb * tb / 2
    else:
        delta_b = tb * v - g * tb * tb / 2
    position[1] = delta_b / 10


def draw_pyramid():
    global wireframe_mode, levels
    global rotateA, rotateB, rotateC
    global rotateX, rotateY, rotateZ
    global position, pyramid_position
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

    glTranslatef(x + position[0], y + position[1], z + position[2])

    glLineWidth(2.0)

    k = size / levels
    l = size

    rotateX = rotateA / levels
    rotateY = rotateB / levels

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

    glBegin(mode)
    glVertex3f(points[levels - 1][0], points[levels - 1][1], points[levels - 1][2])
    glVertex3f(x / 2, y, z / 2)
    glVertex3f(points[levels - 1][3], points[levels - 1][4], points[levels - 1][5])
    glEnd()

    glBegin(mode)
    glVertex3f(x / 2, y, z / 2)
    glVertex3f(points[levels - 1][6], points[levels - 1][7], points[levels - 1][8])
    glVertex3f(points[levels - 1][9], points[levels - 1][10], points[levels - 1][11])
    glEnd()

    glBegin(mode)
    glVertex3f(points[levels - 1][0], points[levels - 1][1], points[levels - 1][2])
    glVertex3f(x / 2, y, z / 2)
    glVertex3f(points[levels - 1][9], points[levels - 1][10], points[levels - 1][11])
    glEnd()

    glBegin(mode)
    glVertex3f(points[levels - 1][3], points[levels - 1][4], points[levels - 1][5])
    glVertex3f(x / 2, y, z / 2)
    glVertex3f(points[levels - 1][9], points[levels - 1][10], points[levels - 1][11])
    glEnd()

    for i in range(levels):
        # bottom
        glBegin(mode)
        glVertex3f(points[i - 1][0], points[i - 1][1], points[i - 1][2])
        glVertex3f(points[i - 1][3], points[i - 1][4], points[i - 1][5])
        glVertex3f(points[i - 1][6], points[i - 1][7], points[i - 1][8])
        glVertex3f(points[i - 1][9], points[i - 1][10], points[i - 1][11])
        glEnd()

        # front
        glBegin(mode)
        glVertex3f(points[i - 1][3], points[i - 1][4], points[i - 1][5])
        glVertex3f(points[i][3], points[i][4], points[i][5])
        glVertex3f(points[i][6], points[i][7], points[i][8])
        glVertex3f(points[i - 1][6], points[i - 1][7], points[i - 1][8])
        glEnd()

        # left
        glBegin(mode)
        glVertex3f(points[i - 1][0], points[i - 1][1], points[i - 1][2])
        glVertex3f(points[i][0], points[i][1], points[i][2])
        glVertex3f(points[i][3], points[i][4], points[i][5])
        glVertex3f(points[i - 1][3], points[i - 1][4], points[i - 1][5])
        glEnd()

        # back
        glBegin(mode)
        glVertex3f(points[i - 1][0], points[i - 1][1], points[i - 1][2])
        glVertex3f(points[i][0], points[i][1], points[i][2])
        glVertex3f(points[i][9], points[i][10], points[i][11])
        glVertex3f(points[i - 1][9], points[i - 1][10], points[i - 1][11])
        glEnd()

        # right
        glBegin(mode)
        glVertex3f(points[i - 1][6], points[i - 1][7], points[i - 1][8])
        glVertex3f(points[i][6], points[i][7], points[i][8])
        glVertex3f(points[i][9], points[i][10], points[i][11])
        glVertex3f(points[i - 1][9], points[i - 1][10], points[i - 1][11])
        glEnd()

    glBegin(mode)
    glVertex3f(points[levels - 1][0], points[levels - 1][1], points[levels - 1][2])
    glVertex3f(points[levels - 1][3], points[levels - 1][4], points[levels - 1][5])
    glVertex3f(points[levels - 1][6], points[levels - 1][7], points[levels - 1][8])
    glVertex3f(points[levels - 1][9], points[levels - 1][10], points[levels - 1][11])
    glEnd()


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
    global animation_mode, texture_mode, spotlight_mode
    global position, t_start, first_b

    if texture_mode:
        glEnable(tex.target)
    else:
        glDisable(tex.target)

    window.clear()
    glClearColor(0.0, 0.0, 0.0, 1.0)
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

    Light = light()
    Light.lightning()

    if animation_mode:
        if first_b:
            t_start = t
            first_b = False
        animate(dt)

    glPushMatrix()
    glScalef(scale, scale, scale)
    glRotatef(rotateA, 1.0, 0.0, 0.0)
    glRotatef(rotateB, 0.0, 1.0, 0.0)
    glRotatef(rotateC, 0.0, 0.0, 1.0)
    draw_pyramid()
    glPopMatrix()


@window.event()
def on_key_press(key, modifiers):
    global wireframe_mode, animation_mode, first_b
    global spotlight_mode, texture_mode
    global scale, x, y, z, levels
    global rotateA, rotateB, rotateC
    global save, cutoff, cutoff_delta

    if key == pyglet.window.key.SPACE:
        wireframe_mode = not wireframe_mode
    if key == pyglet.window.key.BACKSPACE:
        rotateA, rotateB, rotateC = 0.0, 0.0, 0.0
        scale = 1.0
        x, y, z = 0.0, 5.0, 0.0
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
    if key == pyglet.window.key.Z:
        rotateC -= 2.0
    if key == pyglet.window.key.C:
        rotateC += 2.0
    if key == pyglet.window.key._1:
        animation_mode = not animation_mode
        first_b = not first_b
    if key == pyglet.window.key._2:
        texture_mode = not texture_mode
    if key == pyglet.window.key._3:
        spotlight_mode = not spotlight_mode
    if key == pyglet.window.key._9 and cutoff > 5:
        cutoff -= 5
    if key == pyglet.window.key._0 and cutoff < 90:
        cutoff += 5
    if key == pyglet.window.key._7 and cutoff_delta > 1:
        cutoff_delta -= 1
    if key == pyglet.window.key._8 and cutoff_delta < 100:
        cutoff_delta += 1
    if key == pyglet.window.key.L:
        save.load()
    if key == pyglet.window.key.K:
        save.save()


class light(object):
    def __init__(self):
        global cutoff_delta, cutoff
        self.color_light = [0.1, 0.0, 0.0, 1]

        self.diffuse_light = [0.0, 0.4, 0.4, 1.0]
        self.position_light = [30, 30, 0, 1.0]
        self.ambient_light = [0.0, 0.3, 0.1, 1.0]
        self.specular_light = [0, 0.1, 0.3, 1.0]

        self.spot_cutoff_light = cutoff
        self.spot_spot_exponent_light = cutoff_delta
        self.spot_direction_light = [-1.0, -1.0, 0]

        self.diffuse_material = [1, 1, 1, 1.0]
        self.ambient_material = [0.2, 0.2, 0.2, 1.0]
        self.specular_material = [0, 1, 1, 1]
        self.shininess_material = 90

    def lightning(self):
        global spotlight_mode

        glEnable(GL_LIGHTING)
        glEnable(GL_LIGHT0)
        glEnable(GL_NORMALIZE)
        glEnable(GL_COLOR_MATERIAL)

        glLightModelfv(GL_LIGHT_MODEL_AMBIENT, (GLfloat * 4)(*self.color_light))

        glLightfv(GL_LIGHT0, GL_POSITION, (GLfloat * 4)(*self.position_light))
        glLightfv(GL_LIGHT0, GL_DIFFUSE, (GLfloat * 4)(*self.diffuse_light))
        glLightfv(GL_LIGHT0, GL_AMBIENT, (GLfloat * 4)(*self.ambient_light))
        glLightfv(GL_LIGHT0, GL_SPECULAR, (GLfloat * 4)(*self.specular_light))

        if spotlight_mode:
            glLightfv(GL_LIGHT0, GL_SPOT_CUTOFF, (GLfloat)(self.spot_cutoff_light))
            glLightfv(GL_LIGHT0, GL_SPOT_EXPONENT, (GLfloat)(self.spot_spot_exponent_light))
            glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, (GLfloat * 3)(*self.spot_direction_light))
        else:
            glLightfv(GL_LIGHT0, GL_SPOT_CUTOFF, (GLfloat)(180))
            glLightfv(GL_LIGHT0, GL_SPOT_EXPONENT, (GLfloat)(0))
            glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, (GLfloat * 3)(*self.spot_direction_light))
        glMaterialfv(GL_FRONT, GL_SHININESS, (GLfloat)(self.shininess_material))

        glMaterialfv(GL_FRONT, GL_DIFFUSE, (GLfloat * 4)(*self.diffuse_material))
        glMaterialfv(GL_FRONT, GL_AMBIENT, (GLfloat * 4)(*self.ambient_material))
        glMaterialfv(GL_FRONT, GL_SPECULAR, (GLfloat * 4)(*self.specular_material))


image = pyglet.image.load(os.path.join(os.getcwd(), "pic.bmp"))
tex = image.get_texture()
glBindTexture(tex.target, tex.id)

glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0,
             GL_RGBA, GL_UNSIGNED_BYTE, image.get_image_data().get_data("RGBA", image.width * 4))

glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE)


class Save:
    def __init__(self, name="save.pickle"):
        self.name = name

    def save(self):
        global x, y, z
        global rotateA, rotateB, rotateC
        global change, scale, levels
        global wireframe_mode, animation_mode, texture_mode
        global spotlight_mode, first_b, up
        global pyramid_position, position
        global t, dt, t_start, v, g
        global delta_b, delta, cutoff, cutoff_delta
        with open(self.name, "wb") as file:
            pickle.dump(x, file)
            pickle.dump(y, file)
            pickle.dump(z, file)
            pickle.dump(rotateA, file)
            pickle.dump(rotateB, file)
            pickle.dump(rotateC, file)
            pickle.dump(wireframe_mode, file)
            pickle.dump(animation_mode, file)
            pickle.dump(texture_mode, file)
            pickle.dump(spotlight_mode, file)
            pickle.dump(first_b, file)
            pickle.dump(up, file)
            pickle.dump(pyramid_position, file)
            pickle.dump(position, file)
            pickle.dump(t, file)
            pickle.dump(dt, file)
            pickle.dump(t_start, file)
            pickle.dump(v, file)
            pickle.dump(g, file)
            pickle.dump(delta, file)
            pickle.dump(delta_b, file)
            pickle.dump(cutoff, file)
            pickle.dump(cutoff_delta, file)

    def load(self):
        global x, y, z
        global rotateA, rotateB, rotateC
        global change, scale, levels
        global wireframe_mode, animation_mode, texture_mode
        global spotlight_mode, first_b, up
        global pyramid_position, position
        global t, dt, t_start, v, g
        global delta_b, delta, cutoff, cutoff_delta
        with open(self.name, "rb") as file:
            x = pickle.load(file)
            y = pickle.load(file)
            z = pickle.load(file)
            rotateA = pickle.load(file)
            rotateB = pickle.load(file)
            rotateC = pickle.load(file)
            wireframe_mode = pickle.load(file)
            animation_mode = pickle.load(file)
            texture_mode = pickle.load(file)
            spotlight_mode = pickle.load(file)
            first_b = pickle.load(file)
            up = pickle.load(file)
            pyramid_position = pickle.load(file)
            position = pickle.load(file)
            t = pickle.load(file)
            dt = pickle.load(file)
            t_start = pickle.load(file)
            v = pickle.load(file)
            g = pickle.load(file)
            delta = pickle.load(file)
            delta_b = pickle.load(file)
            cutoff = pickle.load(file)
            cutoff_delta = pickle.load(file)


save = Save("data.pickle")

pyglet.clock.schedule(func=upd)
app.run()

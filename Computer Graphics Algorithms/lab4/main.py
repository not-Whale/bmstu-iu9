import pyglet
from pyglet.window import mouse
from pyglet import app
from pyglet.gl import *

# создание окна
window = pyglet.window.Window(500, 500, caption="lab4")

# ограничение его размеров
window.set_minimum_size(200, 200)
window.set_maximum_size(1000, 1000)

# установка параметров курсора
cursor = window.get_system_mouse_cursor(window.CURSOR_CROSSHAIR)
window.set_mouse_cursor(cursor)

# установка цвета фона
glClearColor(0.0, 0.0, 0.0, 0.0)

# создание массива для точек и буфера кадра
points_list = []
frame_buffer = []

# установка размеров окна
screen_width = 500
screen_height = 500

# заполнения буфера кадра
for i in range(screen_width * screen_height * 3):
    frame_buffer.append(0.0)

# флаги заполнения и сглаживания
filling_mode = False
smoothing_mode = False


# создание класса точки
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y


# запись пикселя в буфер кадра
def set_pixel(x, y, r, g, b):
    index = 3 * x + 3 * y * screen_width
    frame_buffer[index] = r
    frame_buffer[index + 1] = g
    frame_buffer[index + 2] = b


# очистка буфера кадра
def clear_buffer():
    frame_buffer.clear()
    for i in range(screen_width * screen_height * 3):
        frame_buffer.append(0.0)


# обработчик изменения размеров окна
def set_resize():
    glViewport(0, 0, screen_width, screen_height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()

    gluOrtho2D(0, screen_width, 0, screen_height)
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()

    clear_buffer()
    points_list.clear()


# модуль
def sign(a):
    if a > 0:
        return 1
    if a < 0:
        return -1
    else:
        return 0


# алгоритм березенхема для построения прямых линий на целых числах
def bresenham(x1, y1, x2, y2):
    dx = abs(x2 - x1)
    dy = abs(y2 - y1)

    sign_x = sign(x2 - x1)
    sign_y = sign(y2 - y1)

    e = 2 * dy - dx

    x = x1
    y = y1

    if dy == 0:
        i = x1
        while abs(x2 - i) > 0:
            set_pixel(i, y, 1.0, 1.0, 1.0)
            i += sign_x
        return

    if dx == 0:
        i = y1
        while abs(y2 - i) > 0:
            set_pixel(x, i, 1.0, 1.0, 1.0)
            i += sign_y
        return

    swap = False
    if dy >= dx:
        dx, dy = dy, dx
        swap = True

    i = 0
    condition = True
    while condition:
        set_pixel(x, y, 1.0, 1.0, 1.0)
        if e < dx:
            if swap:
                y += sign_y
            else:
                x += sign_x
            e += 2 * dy
        else:
            if swap:
                x += sign_x
            else:
                y += sign_y
            e -= 2 * dx
        i += 1
        if i > dx + dy:
            set_pixel(x, y, 1.0, 1.0, 1.0)
            condition = False


# заполнение восьмисвязной области
def eight_connected_filling(x, y):
    stack = [Point(x, y)]

    while len(stack) > 0:
        p = stack.pop()

        x_min = p.x
        x_max = p.x
        y = p.y

        while True:
            index = 3 * (x_min - 1) + 3 * screen_width * y
            r = frame_buffer[index]
            g = frame_buffer[index + 1]
            b = frame_buffer[index + 2]
            if r > 0 and g > 0 and b > 0:
                break
            x_min -= 1
            if x_min <= 0:
                x_min = 0
                break

        while True:
            index = 3 * (x_max + 1) + 3 * screen_width * y
            r = frame_buffer[index]
            g = frame_buffer[index + 1]
            b = frame_buffer[index + 2]
            if r > 0 and g > 0 and b > 0:
                break
            x_max += 1
            if x_max >= screen_width - 1:
                x_max = screen_width - 1
                break

        for k in range(x_min, x_max + 1):
            set_pixel(k, y, 1.0, 1.0, 1.0)

        if y - 1 > 0:
            flag = True

            if x_min > 0:
                i_min = x_min - 1
            else:
                i_min = 0

            if x_max < screen_width - 2:
                i_max = x_max + 1
            else:
                i_max = screen_width - 1

            for k in range(i_min, i_max + 1):
                index = 3 * k + 3 * screen_width * (y - 1)
                r = frame_buffer[index]
                g = frame_buffer[index + 1]
                b = frame_buffer[index + 2]
                if r == 0 and g == 0 and b == 0:
                    if flag:
                        stack.append(Point(k, y - 1))
                        flag = False
                    else:
                        pass
                else:
                    flag = True

        if y + 1 < screen_height:
            flag = True

            if x_min > 0:
                i_min = x_min - 1
            else:
                i_min = 0

            if x_max < screen_width - 2:
                i_max = x_max + 1
            else:
                i_max = screen_width - 1

            for k in range(i_min, i_max + 1):
                index = 3 * k + 3 * screen_width * (y + 1)
                r = frame_buffer[index]
                g = frame_buffer[index + 1]
                b = frame_buffer[index + 2]
                if r == 0 and g == 0 and b == 0:
                    if flag:
                        stack.append(Point(k, y + 1))
                        flag = False
                    else:
                        pass
                else:
                    flag = True


# постфильтрация - сглаживание
def smooth_post_filtration():
    new_width = 2 * screen_width + 1
    new_height = 2 * screen_height + 1
    new_frame_buffer = []
    for i in range(new_width * new_height):
        new_frame_buffer.append(0.0)

    for i in range(screen_height):
        for j in range(screen_width):
            index = 3 * i * screen_width + 3 * j
            r = frame_buffer[index]
            g = frame_buffer[index + 1]
            b = frame_buffer[index + 2]
            if r == 1 and g == 1 and b == 1:
                new_index = (2 * i + 1) * new_width + (2 * j + 1)

                ni = new_index
                new_frame_buffer[ni] = 1.0

                ni = new_index - new_width - 1
                new_frame_buffer[ni] = 1.0
                ni = new_index - new_width
                new_frame_buffer[ni] = 1.0
                ni = new_index - new_width + 1
                new_frame_buffer[ni] = 1.0

                ni = new_index + new_width - 1
                new_frame_buffer[ni] = 1.0
                ni = new_index + new_width
                new_frame_buffer[ni] = 1.0
                ni = new_index + new_width + 1
                new_frame_buffer[ni] = 1.0

                ni = new_index - 1
                new_frame_buffer[ni] = 1.0

                ni = new_index + 1
                new_frame_buffer[ni] = 1.0

    for i in range(screen_height):
        for j in range(screen_width):
            index = 3 * i * screen_width + 3 * j
            intens = 0

            new_index = (2 * i + 1) * new_width + (2 * j + 1)

            ni = new_index
            intens += new_frame_buffer[ni]

            ni = new_index - new_width - 1
            intens += new_frame_buffer[ni]
            ni = new_index - new_width
            intens += new_frame_buffer[ni]
            ni = new_index - new_width + 1
            intens += new_frame_buffer[ni]

            ni = new_index + new_width - 1
            intens += new_frame_buffer[ni]
            ni = new_index + new_width
            intens += new_frame_buffer[ni]
            ni = new_index + new_width + 1
            intens += new_frame_buffer[ni]

            ni = new_index - 1
            intens += new_frame_buffer[ni]

            ni = new_index + 1
            intens += new_frame_buffer[ni]

            frame_buffer[index] = intens / 9
            frame_buffer[index + 1] = intens / 9
            frame_buffer[index + 2] = intens / 9


# отрисовка
def draw_lines():
    if len(points_list) == 2:
        bresenham(points_list[0].x, points_list[0].y, points_list[1].x, points_list[1].y)
    elif len(points_list) > 2:
        for i in range(len(points_list) - 1):
            bresenham(points_list[i].x, points_list[i].y, points_list[i + 1].x, points_list[i + 1].y)
        bresenham(points_list[len(points_list) - 1].x, points_list[len(points_list) - 1].y,
                  points_list[0].x, points_list[0].y)
    if smoothing_mode:
        smooth_post_filtration()


# обработчик нажатий мыши
@window.event()
def on_mouse_press(x, y, button, modifiers):
    global points_list, filling_mode
    if button & mouse.LEFT:
        filling_mode = False
        clear_buffer()
        # fuses_list.clear()
        points_list.append(Point(x, y))
        # print("Point added:", x, ", ", y)
        set_pixel(x, y, 1.0, 1.0, 1.0)
        draw_lines()
    elif button & mouse.RIGHT:
        if len(points_list) > 2:
            eight_connected_filling(x, y)
            filling_mode = True


# отрисовка
@window.event()
def on_draw():
    window.clear()
    glDrawPixels(screen_width, screen_height, GL_RGB, GL_FLOAT, (GLfloat * len(frame_buffer))(*frame_buffer))
    window.flip()


# обработчик на изменение размеров окна
@window.event()
def on_resize(width, height):
    global screen_width, screen_height
    screen_width, screen_height = width, height
    set_resize()


# обработчик нажатия на клавиши
@window.event()
def on_key_press(key, modifiers):
    global filling_mode, smoothing_mode, points_list
    if key == pyglet.window.key.D:
        if len(points_list) > 0:
            filling_mode = False
            points_list.pop()
            clear_buffer()
            draw_lines()
    if key == pyglet.window.key.DELETE:
        points_list.clear()
        filling_mode = False
        clear_buffer()
    if key == pyglet.window.key.S:
        smoothing_mode = not smoothing_mode
        clear_buffer()
        draw_lines()
    if key == pyglet.window.key.F:
        filling_mode = False
        clear_buffer()
        draw_lines()


app.run()

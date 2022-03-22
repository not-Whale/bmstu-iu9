import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Figure {
    // Треугольник или прямоугольник
    public static boolean isTriangle = true;
    // Флаг цвета 0 - черный, 1 - желтый, 2 - белый
    public static int color = 0;
    // Соответствующие red, blue, green переменные
    private static int r, g, b;

    public static void config(GL2 gl2, int width, int height) {
        // Устанавливаем цвет фона
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        gl2.glClearColor(0.95f, 0.95f, 0.85f, 1f);

        // Вводим систему координат
        GLU glu = new GLU();
        glu.gluOrtho2D( 0.0f, width, 0.0f, height);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
        gl2.glViewport(0, 0, width, height);
    }

    public static void draw(GL2 gl2, int width, int height) {
        // Очистка буфера
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
        // Изменение rgb под необходимый цвет
        changeColor();
        // Вычисление отступа от края
        float padding = 0.05f * Math.min(width, height);

        //System.out.println(r + " " + g + " " + b + " " + isTriangle);

        // Отрисовка треугольника
        if (isTriangle) {
            gl2.glLoadIdentity();
            // Рисуем треугольник
            gl2.glBegin(GL2.GL_TRIANGLES);
            // Выбираем цвет
            gl2.glColor3f(r, g, b);
            // Ставим три точки-вершины
            gl2.glVertex2f(padding, padding);
            gl2.glVertex2f(width - padding, padding);
            gl2.glVertex2f((float)(width / 2), height - padding);
        } else {
            // Рисуем прямоугольник
            gl2.glBegin(GL2.GL_QUADS);
            // Выбираем цвет
            gl2.glColor3f(r, g, b);
            // Ставим четыре точки-вершины
            gl2.glVertex2f(padding, padding);
            gl2.glVertex2f(padding, height - padding);
            gl2.glVertex2f(width - padding, height - padding);
            gl2.glVertex2f(width - padding, padding);
        }
        // Закончили рисовать
        gl2.glEnd();
    }

    private static void changeColor() {
        if (color == 0) {
            // Черный
            r = 0;
            g = 0;
            b = 0;
        } else if (color == 1) {
            // Желтый
            r = 255;
            g = 255;
            b = 0;
        } else if (color == 2) {
            // Белый
            r = 255;
            g = 255;
            b = 255;
        }
    }
}

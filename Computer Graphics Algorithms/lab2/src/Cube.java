import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Cube {
    private static float x = 0.0f;
    private static float y = 0.0f;
    private static float z = 0.0f;
    private static float width = 0.5f;
    private static boolean wireframeMode = true;

    public static float getX() {
        return x;
    }

    public static float getY() {
        return y;
    }

    public static float getZ() {
        return z;
    }

    public static void setX(float new_x) {
        x = new_x;
    }

    public static void setY(float new_y) {
        y = new_y;
    }

    public static void setZ(float new_z) {
        z = new_z;
    }

    public static void incWidth() {
        width += 0.05;
    }

    public static void decWidth() {
        if (width > 0.05) {
            width -= 0.05;
        }
    }

    // рамочный режим
    public static void changeWireframeMod() {
        wireframeMode = !wireframeMode;
    }

    // установка рамочного режима
    private static void setWireframeMode(GL2 gl) {
        if (wireframeMode) {
            gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_LINE);
        } else {
            gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_FILL);
        }
    }

    // отрисовка параллелепипеда
    private static void drawParallelepiped(float x, float y, float z, float width, float height, float depth, GL2 gl, int type) {
        float r, g, b;

        r = 0.0f;
        g = 0.0f;
        b = 0.0f;
        gl.glColor3f(r, g, b);
        gl.glBegin(type);

        gl.glVertex3f(depth + x, -width + y, height + z);
        gl.glVertex3f(-depth + x, -width + y, height + z);
        gl.glVertex3f(-depth + x, width + y, height + z);
        gl.glVertex3f(depth + x, width + y, height + z);
        gl.glEnd();

        r = 0.0f;
        g = 0.0f;
        b = 1.0f;
        gl.glColor3f(r, g, b);
        gl.glBegin(type);

        gl.glVertex3f(depth + x, -width + y, -height + z);
        gl.glVertex3f(-depth + x, -width + y, -height + z);
        gl.glVertex3f(-depth + x, width + y, -height + z);
        gl.glVertex3f(depth + x, width + y, -height + z);
        gl.glEnd();

        r = 0.0f;
        g = 1.0f;
        b = 0.0f;
        gl.glBegin(type);
        gl.glColor3f(r, g, b);
        gl.glVertex3f(depth + x, -width + y, -height + z);
        gl.glVertex3f(depth + x, -width + y, height + z);
        gl.glVertex3f(depth + x, width + y, height + z);
        gl.glVertex3f(depth + x, width + y, -height + z);
        gl.glEnd();

        r = 0.0f;
        g = 1.0f;
        b = 1.0f;
        gl.glBegin(type);
        gl.glColor3f(r, g, b);
        gl.glVertex3f(-depth + x, -width + y, -height + z);
        gl.glVertex3f(-depth + x, -width + y, height + z);
        gl.glVertex3f(-depth + x, width + y, height + z);
        gl.glVertex3f(-depth + x, width + y, -height + z);
        gl.glEnd();

        r = 1.0f;
        g = 0.0f;
        b = 0.0f;
        gl.glBegin(type);
        gl.glColor3f(r, g, b);
        gl.glVertex3f(-depth + x, width + y, -height + z);
        gl.glVertex3f(-depth + x, width + y, height + z);
        gl.glVertex3f(depth + x, width + y, height + z);
        gl.glVertex3f(depth + x, width + y, -height + z);
        gl.glEnd();

        r = 1.0f;
        g = 0.0f;
        b = 1.0f;
        gl.glBegin(type);
        gl.glColor3f(r, g, b);
        gl.glVertex3f(-depth + x, -width + y, -height + z);
        gl.glVertex3f(-depth + x, -width + y, height + z);
        gl.glVertex3f(depth + x, -width + y, height + z);
        gl.glVertex3f(depth + x, -width + y, -height + z);
        gl.glEnd();
    }

    // отрисовка куба
    private static void drawCube(float x, float y, float z, float a, GL2 gl, int type) {
        drawParallelepiped(x, y, z, a, a, a, gl, type);
    }

    // обработка ресайза окна
    public static void reshape(GL2 gl, int width, int height) {
        // очистка буфера цвета и устновка нового
        gl.glClearColor(0.95f, 0.95f, 0.85f, 1f);
        // установка матрицы, которая будет изменяться, проекционной
        // (работа с перспективой)
        gl.glMatrixMode(GL2.GL_PROJECTION_MATRIX);
        // установка проекционной матрицы в единичную
        gl.glLoadIdentity();

        // установка перпективы
        GLU glu = new GLU();
        // угол визуального охвата = 45 градусов
        // отношение сторон порта просмотра стандартное
        // ближняя и дальняя проскости просмотра 0.1 и 100
        glu.gluPerspective(45, (double)width / (double)height, 0.1, 100);

        // установка матрицы, которая будет изменяться, объектно-видовой
        // (положение объекта относительно камеры)
        gl.glMatrixMode(GL2.GL_MODELVIEW_MATRIX);
        // перенос начала системы координат
        gl.glTranslatef(0, 0, -5);

        // установка области видимости
        gl.glViewport( 0, 0, width, height );
    }

    // отрисовка статичного маленького кубика
    public static void drawSmallCube(GL2 gl) {
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        int type = GL2.GL_POLYGON;

        setWireframeMode(gl);
        gl.glLineWidth(1f);

        drawCube(-1.2f, -1.2f, 0.75f, 0.2f, gl, type);
    }

    // отрисовка большого динамичного куба
    public static void drawBigCube(GL2 gl) {
        int type = GL2.GL_POLYGON;

        setWireframeMode(gl);

        gl.glPushMatrix();
        gl.glRotated(x, 1.0, 0.0, 0.0);
        gl.glRotated(y, 0.0, 1.0, 0.0);
        gl.glRotated(z, 0.0, 0.0, 1.0);
        gl.glLineWidth(2.5f);

        drawCube(0f, 0f, 0f, width, gl, type);

        gl.glPopMatrix();
    }
}

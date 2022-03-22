import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CanvasAWT {
    public static void main (String []args) {
        // Создаем канвас
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas canvas = new GLCanvas(capabilities);

        // Новый слушатель
        canvas.addGLEventListener(new GLEventListener() {

            // Изменение размера окна
            @Override
            public void reshape(GLAutoDrawable auto_drawable, int x, int y, int width, int height) {
                Figure.config(auto_drawable.getGL().getGL2(), width, height);
            }

            // Это нам не надо
            @Override
            public void init(GLAutoDrawable auto_drawable) {}

            // Это нам не надо
            @Override
            public void dispose( GLAutoDrawable glautodrawable ) {}

            // Отображение
            @Override
            public void display(GLAutoDrawable auto_drawable) {
                Figure.draw(auto_drawable.getGL().getGL2(), auto_drawable.getSurfaceWidth(), auto_drawable.getSurfaceHeight());
            }
        });

        // Создаем слушатель действий мыши
        canvas.addMouseListener(new MouseListener() {

            // На нажатие
            @Override
            public void mouseClicked(MouseEvent e) {
                // ЛКМ
                if (e.getButton() == 1) {
                    Figure.isTriangle = !Figure.isTriangle;
                    canvas.display();
                } else
                // ПКМ
                    if (e.getButton() == 3) {
                    Figure.color = (Figure.color + 1) % 3;
                    //System.out.println("color = " + Figure.color);
                    canvas.display();
                }
            }

            // Это нам не надо
            @Override
            public void mousePressed(MouseEvent e) {}

            // Это нам не надо
            @Override
            public void mouseReleased(MouseEvent e) {}

            // Это нам не надо
            @Override
            public void mouseEntered(MouseEvent e) {}

            // Это нам не надо
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        // Создание рамки
        final Frame frame = new Frame("Complex Triangle");
        // Добавляем в нее канвас
        frame.add(canvas);
        // Добавим слушатель окна
        frame.addWindowListener(new WindowAdapter() {
            // Обработаем закрытие окна
            // Чтобы
            // 1) Удалялся канвас
            // 2) Закрывалась рамка
            // 3) Завершалась программа
            public void windowClosing(WindowEvent e) {
                frame.remove(canvas);
                frame.dispose();
                System.exit(0);
            }
        });
        // Установим размер
        frame.setSize(1080, 720);
        // Включим видимость
        frame.setVisible(true);
    }
}

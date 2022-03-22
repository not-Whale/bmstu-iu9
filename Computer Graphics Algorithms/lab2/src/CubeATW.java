import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CubeATW {
    public static void main(String []args) {
        // создание канваса
        final GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas canvas = new GLCanvas(capabilities);

        // слушатель для изменения параметров окна
        canvas.addGLEventListener(new GLEventListener() {

            @Override
            public void init(GLAutoDrawable glAutoDrawable) {
            }

            @Override
            public void dispose(GLAutoDrawable glAutoDrawable) {
            }

            @Override
            public void display(GLAutoDrawable glAutoDrawable) {
                Cube.drawSmallCube(glAutoDrawable.getGL().getGL2());
                Cube.drawBigCube(glAutoDrawable.getGL().getGL2());
            }

            @Override
            public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
                Cube.reshape(glAutoDrawable.getGL().getGL2(), width, height);
            }
        });

        // слушатель нажатия на клавиши
        canvas.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            // повороты куба
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        Cube.setX(Cube.getX() - 10.0f);
                        break;
                    case KeyEvent.VK_S:
                        Cube.setX(Cube.getX() + 10.0f);
                        break;
                    case KeyEvent.VK_D:
                        Cube.setY(Cube.getY() + 10.0f);
                        break;
                    case KeyEvent.VK_A:
                        Cube.setY(Cube.getY() - 10.0f);
                        break;
                    case KeyEvent.VK_Q:
                        Cube.setZ(Cube.getZ() + 10.0f);
                        break;
                    case KeyEvent.VK_E:
                        Cube.setZ(Cube.getZ() - 10.0f);
                        break;
                    case KeyEvent.VK_K:
                        Cube.changeWireframeMod();
                        break;
                    case KeyEvent.VK_UP:
                        Cube.incWidth();
                        break;
                    case KeyEvent.VK_DOWN:
                        Cube.decWidth();
                        break;
                }
                canvas.display();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

        // создание окна
        final Frame frame = new Frame("Cube Projection");
        frame.add(canvas);
        // слушатель на закрытие окна
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.remove(canvas);
                frame.dispose();
                System.exit(0);
            }
        });

        // отображение окна
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}

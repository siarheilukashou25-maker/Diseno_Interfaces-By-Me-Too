import com.bymetoo.componentes.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.assertj.swing.core.*;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;

public class Integracion {
    static final String[] VISTAS = {"vista.MainView","vista.UserView","vista.GroupView","vista.RepartosView","vista.CompraWindow","vista.ResumenView"};
    static Robot robot;
    public static void main(String[] a) throws Exception {
        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(30);
        String out = a[0];
        for (String v : VISTAS) {
            Window w = abrir(v);
            List<BotonBymetoo> bs = botones(w);
            System.out.println("\n### " + v + "  (" + bs.size() + " botones)");
            for (BotonBymetoo b : bs) {
                String info = GuiActionRunner.execute(() -> String.format("  %-16s texto=%-13s estilo=%-14s estado=%-9s activo=%-5s destino=%-20s visible=%s tam=%dx%d pref=%dx%d foco=%s name=%s",
                    b.getClass().getSimpleName(), b.getTexto(), b.getEstilo(), b.getEstado(), b.isActivo(), b.getVentanaDestino(), b.isShowing(), b.getWidth(), b.getHeight(), b.getPreferredSize().width, b.getPreferredSize().height, b.isFocusable(), b.getName()));
                System.out.println(info);
            }
            captura(w, out + "/" + v.substring(6) + ".png");
            cerrarTodo();
            // navegación: un clic por botón
            for (int i = 0; i < bs.size(); i++) {
                Window w2 = abrir(v);
                BotonBymetoo b = botones(w2).get(i);
                String dest = GuiActionRunner.execute(b::getVentanaDestino);
                String txt = GuiActionRunner.execute(b::getTexto);
                robot.moveMouse(w2); robot.waitForIdle();
                robot.click(b, MouseButton.LEFT_BUTTON);
                robot.waitForIdle(); Thread.sleep(400); robot.waitForIdle();
                List<String> vis = visibles();
                boolean origenAbierto = GuiActionRunner.execute(w2::isShowing);
                long n = vis.stream().filter(s -> s.equals(dest)).count();
                String r = dest.equals(v) ? (vis.size()==1 && origenAbierto ? "OK (ya estás, no abre otra)" : "MAL")
                        : (n==1 && !origenAbierto && vis.size()==1 ? "OK" : "MAL");
                System.out.printf("  clic %-13s -> %-20s visibles=%s  %s%n", txt, dest, vis, r);
                cerrarTodo();
            }
            // doble clic sobre el primer botón con destino distinto
            for (int i = 0; i < bs.size(); i++) {
                Window w3 = abrir(v);
                BotonBymetoo b = botones(w3).get(i);
                String dest = GuiActionRunner.execute(b::getVentanaDestino);
                if (dest.equals(v)) { cerrarTodo(); continue; }
                robot.click(b, MouseButton.LEFT_BUTTON, 2);
                robot.waitForIdle(); Thread.sleep(400); robot.waitForIdle();
                System.out.printf("  doble clic %s -> visibles=%s%n", GuiActionRunner.execute(b::getTexto), visibles());
                cerrarTodo();
                break;
            }
        }
        System.exit(0);
    }
    static Window abrir(String v) throws Exception {
        Window w = GuiActionRunner.execute(() -> { try { Window x = (Window) Class.forName(v).getDeclaredConstructor().newInstance(); x.setLocation(0,0); x.setVisible(true); return x; } catch (Exception e) { throw new RuntimeException(e); } });
        robot.waitForIdle(); Thread.sleep(300); robot.waitForIdle();
        return w;
    }
    static List<BotonBymetoo> botones(Container c) {
        return GuiActionRunner.execute(() -> { List<BotonBymetoo> l = new ArrayList<>(); recoger(c, l); return l; });
    }
    static void recoger(Component c, List<BotonBymetoo> l) {
        if (c instanceof BotonBymetoo b) l.add(b);
        if (c instanceof Container k) for (Component h : k.getComponents()) recoger(h, l);
    }
    static List<String> visibles() {
        return GuiActionRunner.execute(() -> { List<String> l = new ArrayList<>(); for (Window w : Window.getWindows()) if (w.isShowing()) l.add(w.getClass().getName()); return l; });
    }
    static void cerrarTodo() { GuiActionRunner.execute(() -> { for (Window w : Window.getWindows()) w.dispose(); }); robot.waitForIdle(); }
    static void captura(Window w, String f) throws Exception {
        BufferedImage img = GuiActionRunner.execute(() -> { BufferedImage i = new BufferedImage(w.getWidth(), w.getHeight(), BufferedImage.TYPE_INT_RGB); Graphics2D g = i.createGraphics(); w.paintAll(g); g.dispose(); return i; });
        ImageIO.write(img, "png", new File(f));
    }
}

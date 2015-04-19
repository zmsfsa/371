package org.janusproject.pr20_04_2015;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	 
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	 
	    // Initialize the Arakhne-VM-utility library to
	    // support this Android activity.
	    // The Arakhne-VM-utility library is used by
	    // the Janus kernel.
	    // (see http://www.arakhne.org/arakhneVmutils)
	    try {
	      Android.initialize(this);
	    }
	    catch (AndroidException e) {
	      Log.e("FirstAndroidApplication", e.getLocalizedMessage(), e);
	    }
	 
	    // Create a Janus kernel
	    Kernel janusKernel = Kernels.create();
	 
	    // Create the Hello-World agent		
	    HelloWorldAgent agent = new HelloWorldAgent();
	 
	    // Launch the Hello-World agent
	    janusKernel.launchLightAgent(agent);
	  }
	 
	  protected void onDestroy() {
	    // Comment the following line if you do not want
	    // to kill all the Janus agents each time the
	    // activity is destroyed.
	    Kernels.killAll();
	  }
	 
	  public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.activity_main, menu);
	    return true;
	  }
	 
	}

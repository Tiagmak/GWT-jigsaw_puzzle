package mpjp.client;

import java.util.function.Consumer;

public class MPJPResources {
	
	public class MPJPAudioResource{
		com.google.gwt.core.client.JavaScriptObject audio;
		
		void play() {
			
		}
	}
	
	MPJPResources(){
		
	}	
	
	static String getResourceDir() {
		return null;
	}
	
	static MPJPResources.MPJPAudioResource loadAudio(String audioName){
		
		return null;
	}
	
	static void loadImageElement(String imageName, Consumer<com.google.gwt.dom.client.ImageElement> onLoad) {
		
	}

	static void setResourcesDir(String resourceDir) {
		
	}
}
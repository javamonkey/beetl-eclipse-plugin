package org.beetl.editors;


import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationPresentation;
import org.eclipse.jface.text.source.ImageUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;

public class BeetlProblemAnnotation extends Annotation implements  IAnnotationPresentation{
	static ISharedImages sharedImages=  null;
	private static Image fgErrorImage= null;
	private static boolean fgImagesInitialized= false;
	public BeetlProblemAnnotation( String text) {
		super("org.eclipse.ui.workbench.texteditor.error",false,text);
	}
	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void paint(GC gc, Canvas canvas, Rectangle bounds) {
		initializeImages();
		if (fgErrorImage != null)
			ImageUtilities.drawImage(fgErrorImage, gc, canvas, bounds, SWT.CENTER, SWT.TOP);
		
		
	}
	
	private void initializeImages() {
		if (fgImagesInitialized)
			return;

		
		ISharedImages sharedImages= PlatformUI.getWorkbench().getSharedImages();
	
		fgErrorImage= sharedImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);

		fgImagesInitialized= true;
	}

}

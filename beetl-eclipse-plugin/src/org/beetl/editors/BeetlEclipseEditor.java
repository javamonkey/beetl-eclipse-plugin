package org.beetl.editors;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.internal.preferences.Activator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class BeetlEclipseEditor extends TextEditor {

	public final static String EDITOR_MATCHING_BRACKETS = "matchingBrackets";
	public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

	private String oldEditorId = null;
	private boolean foldingAll = false;

	private ProjectionSupport projectionSupport;
	private Annotation[] oldAnnotations;
	private Annotation[] oldErrorAnnotations;

	private ProjectionAnnotationModel annotationModel;

	IAnnotationModel helpModel = null;

	public BeetlEclipseEditor() {
		super();

		setSourceViewerConfiguration(new BeetlSourceViewerConfiguration(this));
		setDocumentProvider(new BeetlDocumentProvider(this));

	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

		projectionSupport = new ProjectionSupport(viewer,
				getAnnotationAccess(), getSharedColors());
		projectionSupport.install();

		// turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);

		annotationModel = viewer.getProjectionAnnotationModel();
		helpModel = viewer.getVisualAnnotationModel();

		ProjectUtil.foldingDocument(this, (Document) viewer.getDocument());

	}

	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {

		ISourceViewer viewer = new ProjectionViewer(parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles);

		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

	public void addFoldingStructure(Position pos) {
		ProjectionAnnotation annotation = new ProjectionAnnotation();
		if (annotationModel == null)
			return;
		annotationModel.addAnnotation(annotation, pos);
	}

	public void updateFoldingStructure(List positions) {

		Annotation[] annotations = new Annotation[positions.size()];

		// this will hold the new annotations along
		// with their corresponding positions
		HashMap newAnnotations = new HashMap();

		for (int i = 0; i < positions.size(); i++) {
			ProjectionAnnotation annotation = new ProjectionAnnotation();

			newAnnotations.put(annotation, positions.get(i));

			annotations[i] = annotation;
		}

		annotationModel.modifyAnnotations(oldAnnotations, newAnnotations, null);

		oldAnnotations = annotations;
		

	}

	public void updateErrorNode(List positions, List<String> msgs) {

		{

//			HashMap newAnnotations = new HashMap();
//
//			BeetlProblemAnnotation annotation = new BeetlProblemAnnotation(
//					"xxxx");
//
//			newAnnotations.put(annotation, new Position(10));
//
//			((IAnnotationModelExtension) helpModel).replaceAnnotations(null,
//					newAnnotations);
		}
		
		
		Annotation[] tempOldAnnotations = new Annotation[positions.size()];

		HashMap newAnnotations = new HashMap();

		for (int i = 0; i < positions.size(); i++) {
			BeetlProblemAnnotation annotation = new BeetlProblemAnnotation(
					msgs.get(i));

			newAnnotations.put(annotation, positions.get(i));

			tempOldAnnotations[i] = annotation;
		}

		((IAnnotationModelExtension) helpModel).replaceAnnotations(
				oldErrorAnnotations, newAnnotations);
		
		

		this.oldErrorAnnotations = tempOldAnnotations;

	}
	
	public void removeErrorNode(){
		((IAnnotationModelExtension) helpModel).removeAllAnnotations();
		this.oldErrorAnnotations  = null;
	}

	@Override
	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		/*
		 * Set the block matcher
		 */
		support.setCharacterPairMatcher(new BeetlCharacterPairMatcher());
		support.setMatchingCharacterPainterPreferenceKeys(
				EDITOR_MATCHING_BRACKETS, EDITOR_MATCHING_BRACKETS_COLOR);
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(EDITOR_MATCHING_BRACKETS, true);
		store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, "128,128,128");

		/**
		 * 配置编辑器上下文 用来绑定 快捷键
		 */
		IContextService contextService = (IContextService) getSite()
				.getService(IContextService.class);
		contextService.activateContext("beetl-eclipse-plugin.beetlcontext");

		super.configureSourceViewerDecorationSupport(support);
	}

	public void dispose() {

		super.dispose();
	}

	public String getOldEditorId() {
		return oldEditorId;
	}

	public void setOldEditorId(String oldEditorId) {
		this.oldEditorId = oldEditorId;
	}

	public boolean isFoldingAll() {
		return foldingAll;
	}

	public void setFoldingAll(boolean foldingAll) {
		this.foldingAll = foldingAll;
	}

	protected void performSave(boolean overwrite,
			IProgressMonitor progressMonitor) {
		// 总是覆盖，否则，不能保存beetl editor
		super.performSave(true, progressMonitor);
	}

	public ProjectionAnnotationModel getAnnotationModel() {
		return annotationModel;
	}

	protected SourceViewerDecorationSupport getSourceViewerDecorationSupport(
			ISourceViewer viewer) {
		SourceViewerDecorationSupport support = super
				.getSourceViewerDecorationSupport(viewer);
		return support;
	}

}

package org.beetl.editors.property;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

public class BeetlPropertyPage extends PropertyPage {

	public static final String PATH_TITLE = "Path:";
	public static final String OWNER_TITLE = "模板根目录(可以不设置):";
	public  static final String OWNER_PROPERTY = "TemplateRoot";
	public  static final String ST_START = "st_start";
	public  static final String ST_END = "st_end";
	public  static final String PL_START = "pl_start";
	public  static final String PL_END = "pl_end";
	public  static final String HTML_TAG = "html_tag";
	public  static final String HTML_VAR = "html_var";
	
	
	
	
	

	private static final int TEXT_FIELD_WIDTH = 50;

	Text  ownerText;
	Text  stStartText;
	Text  stEndText;
	Text  plStartText;
	Text  plEndText;
	Text  htmlTagText;
	Text  htmlVarText;
	
	
	
	

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public BeetlPropertyPage() {
		super();
	}

	

	private void addSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		separator.setLayoutData(gridData);
		
	}

	private void addSecondSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText(OWNER_TITLE);

		// Owner text field
		ownerText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		ownerText.setLayoutData(gd);
		
		String path="";
		try {
			path = getProject().getProject().getPersistentProperty(
					new QualifiedName("", OWNER_PROPERTY));
			if(path==null){
				path = "";
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ownerText.setText(path);
		// Populate owner text field
		
	}
	
	
	private void addStatementStartSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText("定界开始符号");

		// Owner text field
		 stStartText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		stStartText.setLayoutData(gd);
		
		String path="";
		try {
			path = getProject().getProject().getPersistentProperty(
					new QualifiedName("", ST_START));
			if(path==null){
				path = "<%";
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stStartText.setText(path);
		// Populate owner text field
		
	}
	
	private void addStatementEndSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText("定界符结束号");

		// Owner text field
		 stEndText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		stEndText.setLayoutData(gd);
		
		String path="";
		try {
			path = getProject().getProject().getPersistentProperty(
					new QualifiedName("", ST_END));
			if(path==null){
				path = "%>";
			}
			if(path.indexOf("/r")!=-1||path.indexOf("\n")!=-1){
				path="";
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stEndText.setText(path);
		// Populate owner text field
		
	}
	
	private void addPsSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText("站位起始符号");

		// Owner text field
		this.plStartText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		plStartText.setLayoutData(gd);
		
		String path="";
		try {
			path = getProject().getProject().getPersistentProperty(
					new QualifiedName("", PL_START));
			if(path==null){
				path = "${";
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plStartText.setText(path);
		// Populate owner text field
		
	}

	private void addPeSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText("站位结束符号");

		// Owner text field
		this.plEndText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		plEndText.setLayoutData(gd);
		
		String path="";
		try {
			path = getProject().getProject().getPersistentProperty(
					new QualifiedName("", PL_END));
			if(path==null){
				path = "}";
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plEndText.setText(path);
		// Populate owner text field
		
	}
	
	
	private void addHtmlTagSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText("HTMLTag标记");

		// Owner text field
		this.htmlTagText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		htmlTagText.setLayoutData(gd);
		
		String path="";
		try {
			path = getProject().getProject().getPersistentProperty(
					new QualifiedName("", HTML_TAG));
			if(path==null){
				path = "#";
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		htmlTagText.setText(path);
		// Populate owner text field
		
	}
	
	private void addHtmlVarSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		// Label for owner field
		Label ownerLabel = new Label(composite, SWT.NONE);
		ownerLabel.setText("HTML Var");

		// Owner text field
		this.htmlVarText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = convertWidthInCharsToPixels(TEXT_FIELD_WIDTH);
		htmlVarText.setLayoutData(gd);
		
		String path="";
		try {
			path = getProject().getProject().getPersistentProperty(
					new QualifiedName("", HTML_VAR));
			if(path==null){
				path = "var";
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		htmlVarText.setText(path);
		// Populate owner text field
		
	}
	
	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		addSecondSection(composite);
		this.addSeparator(parent);
		this.addStatementStartSection(parent);
		this.addStatementEndSection(parent);
		this.addPsSection(parent);
		this.addPeSection(parent);
		this.addHtmlTagSection(parent);
		this.addHtmlVarSection(parent);
		
		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	protected void performDefaults() {
		super.performDefaults();
		String path = "";
		
		ownerText.setText(path);
		this.stStartText.setText("<%");
		this.stEndText.setText("%>");
		this.plStartText.setText("${");
		this.plEndText.setText("}");
		this.htmlTagText.setText("#");
		this.htmlVarText.setText("var");
		
		
	}
	
	public boolean performOk() {
		// store the value in the owner text field
		try {
			getProject().getProject().setPersistentProperty(
					new QualifiedName("", OWNER_PROPERTY),
					ownerText.getText());
			getProject().getProject().setPersistentProperty(
					new QualifiedName("", this.ST_START),
					this.stStartText.getText());
				
			getProject().getProject().setPersistentProperty(
					new QualifiedName("", this.ST_END),
					this.stEndText.getText());
			
			getProject().getProject().setPersistentProperty(
					new QualifiedName("", this.PL_START),
					this.plStartText.getText());
				
			getProject().getProject().setPersistentProperty(
					new QualifiedName("", this.PL_END),
					this.plEndText.getText());
			
			getProject().getProject().setPersistentProperty(
					new QualifiedName("", this.HTML_TAG),
					this.htmlTagText.getText());
			
			getProject().getProject().setPersistentProperty(
					new QualifiedName("", this.HTML_VAR),
					this.htmlVarText.getText());
			
		} catch (CoreException e) {
			return false;
		}
		return true;
	}
	
	private  IProject getProject(){
		 IAdaptable adaptable= getElement();
		return adaptable == null ? null : (IProject)adaptable.getAdapter(IProject.class);

	}

}
package ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.HTML;

public class WTPdfViewerWidget extends HTML {

	private JavaScriptObject pdfApplication;

	private Element root;
	private DivElement outerContainer;
	private DivElement mainContainer;
	private DivElement loadingBar;
	private DivElement progress;
	private DivElement viewerContainer;
	private DivElement viewer;
	private DivElement overlayContainer;
	private DivElement passwordOverlay;
	private DivElement documentPropertiesOverlay;
	private String fileName;
	private String password;

	// main toolbar
	private DivElement toolbar;
	private DivElement toolbarContainer;
	private DivElement toolbarViewer;
	private DivElement toolbarViewerLeft;
	private DivElement toolbarViewerMiddle;
	private DivElement toolbarViewerRight;

	// turn on and off left sidebar (the one with thumbnail, outline and attachments)
	private ButtonElement sidebarToggleBtn;

	// left sidebar (the one with thumbnail, outline and attachments)
	private DivElement sidebarContainer;
	private DivElement toolbarSidebar;
	private DivElement sidebarContent;
	private DivElement thumbnailView;
	private DivElement outlineView;
	private DivElement attachmentsView;
	private ButtonElement viewThumbnail;
	private ButtonElement viewOutline;
	private ButtonElement viewAttachments;

	private DivElement toolbarButtonSpacer;

	// turn on and off findbar (little modal window that allows user to search inside a document)
	private DivElement viewFind;

	// findbar - little modal window that allows user to search inside a document
	private DivElement findbar;
	private DivElement findbarInputContainer;
	private InputElement findInput;
	private ButtonElement findPrevious;
	private ButtonElement findNext;
	private DivElement findbarOptionsContainer;
	private InputElement findHighlightAll;
	private LabelElement findHighlightAllLabel;
	private InputElement findMatchCase;
	private LabelElement findMatchCaseLabel;
	private SpanElement findResultsCount;
	private DivElement findbarMessageContainer;
	private SpanElement findMsg;

	// paging section of toolbar 
	private ButtonElement previousPage;
	private ButtonElement nextPage;
	private InputElement pageNumber;
	private SpanElement numPages;

	// zooming (scalling) section of toolbar
	private ButtonElement zoomOut;
	private ButtonElement zoomIn;
	private SpanElement scaleSelectContainer;
	private SelectElement scaleSelect;
	private OptionElement customScaleOption;

	// right section of the toolbar
	private ButtonElement secondaryToolbarToggle;

	// initially hidden toolbar on the right - has to click double arrow to open it
	private DivElement secondaryToolbar;
	private ButtonElement secondaryPresentationMode;
	private ButtonElement secondaryPrint;
	private ButtonElement secondaryDownload;
	private ButtonElement firstPage;
	private ButtonElement lastPage;
	private ButtonElement pageRotateCw;
	private ButtonElement pageRotateCcw;
	private ButtonElement toggleHandTool;
	private ButtonElement documentProperties;

	// document properties overlay
	private ParagraphElement fileNameField;
	private ParagraphElement fileSizeField;
	private ParagraphElement titleField;
	private ParagraphElement authorField;
	private ParagraphElement subjectField;
	private ParagraphElement keywordsField;
	private ParagraphElement creationDateField;
	private ParagraphElement modificationDateField;
	private ParagraphElement creatorField;
	private ParagraphElement producerField;
	private ParagraphElement versionField;
	private ParagraphElement pageCountField;
	private ButtonElement documentPropertiesClose;

	//right toolbar
	private ButtonElement presentationModeBtn;

	// print service overlay - modal window with progress bar
	private DivElement printServiceOverlay;
	private ButtonElement printCancel;

	// pdf about to be printed goes here
	private DivElement printContainer;

	private PdfViewerWidgetErrorListener errorListener;


	public WTPdfViewerWidget() {
		Document document = Document.get();
		root = document.createDivElement();
		root.addClassName("loadingInProgress");

		outerContainer = createChildDiv(document, root, "outerContainer");
		createSidebar(document, outerContainer);

		mainContainer = createChildDiv(document, outerContainer, "mainContainer");
		createFindBar(document, mainContainer);
		//the toolbar on the right that opens after click on double arrow on the right
		createSecondaryToolbar(document, mainContainer);
		createMainToolbar(document, mainContainer);

		loadingBar = createChildDiv_withId(document, mainContainer, "loadingBar");
		progress = createChildDiv_withId(document, loadingBar, "progress");

		createViewerContainer(document, mainContainer);
		// Skip appending print container because it will be appending
		// dynamical at the top of page
		printContainer = createChildDiv(document, root, "printContainer", false);
		printContainer.setId("printContainer");

		setElement(root);
		startWebViewerLoad(this);

		setStyleName("wtpdfviewer");
	}

	public void setDownloadVisible(boolean downloadVisible) {
		Style style = secondaryDownload.getStyle();
		if (downloadVisible) {
			style.clearDisplay();
		} else {
			style.setDisplay(Style.Display.NONE);
		}
	}
	public void setPrintVisible(boolean printVisible) {
		Style style = secondaryPrint.getStyle();
		if (printVisible) {
			style.clearDisplay();
		} else {
			style.setDisplay(Style.Display.NONE);
		}
	}

	/**
	 *
	 <div id="secondaryToolbar" class="secondaryToolbar hidden doorHangerRight">
	 <div id="secondaryToolbarButtonContainer">
	 <button id="secondaryPresentationMode" class="secondaryToolbarButton presentationMode visibleLargeView" title="Switch to Presentation Mode" tabindex="51" data-l10n-id="presentation_mode">
	 <span data-l10n-id="presentation_mode_label">Presentation Mode</span>
	 </button>

	 <button id="secondaryOpenFile" class="secondaryToolbarButton openFile visibleLargeView" title="Open File" tabindex="52" data-l10n-id="open_file">
	 <span data-l10n-id="open_file_label">Open</span>
	 </button>

	 <button id="secondaryPrint" class="secondaryToolbarButton print visibleMediumView" title="Print" tabindex="53" data-l10n-id="print">
	 <span data-l10n-id="print_label">Print</span>
	 </button>

	 <button id="secondaryDownload" class="secondaryToolbarButton download visibleMediumView" title="Download" tabindex="54" data-l10n-id="download">
	 <span data-l10n-id="download_label">Download</span>
	 </button>

	 <a href="#" id="secondaryViewBookmark" class="secondaryToolbarButton bookmark visibleSmallView" title="Current view (copy or open in new window)" tabindex="55" data-l10n-id="bookmark">
	 <span data-l10n-id="bookmark_label">Current View</span>
	 </a>

	 <div class="horizontalToolbarSeparator visibleLargeView"></div>

	 <button id="firstPage" class="secondaryToolbarButton firstPage" title="Go to First Page" tabindex="56" data-l10n-id="first_page">
	 <span data-l10n-id="first_page_label">Go to First Page</span>
	 </button>
	 <button id="lastPage" class="secondaryToolbarButton lastPage" title="Go to Last Page" tabindex="57" data-l10n-id="last_page">
	 <span data-l10n-id="last_page_label">Go to Last Page</span>
	 </button>

	 <div class="horizontalToolbarSeparator"></div>

	 <button id="pageRotateCw" class="secondaryToolbarButton rotateCw" title="Rotate Clockwise" tabindex="58" data-l10n-id="page_rotate_cw">
	 <span data-l10n-id="page_rotate_cw_label">Rotate Clockwise</span>
	 </button>
	 <button id="pageRotateCcw" class="secondaryToolbarButton rotateCcw" title="Rotate Counterclockwise" tabindex="59" data-l10n-id="page_rotate_ccw">
	 <span data-l10n-id="page_rotate_ccw_label">Rotate Counterclockwise</span>
	 </button>

	 <div class="horizontalToolbarSeparator"></div>

	 <button id="toggleHandTool" class="secondaryToolbarButton handTool" title="Enable hand tool" tabindex="60" data-l10n-id="hand_tool_enable">
	 <span data-l10n-id="hand_tool_enable_label">Enable hand tool</span>
	 </button>

	 <div class="horizontalToolbarSeparator"></div>

	 <button id="documentProperties" class="secondaryToolbarButton documentProperties" title="Document Propertiesâ€¦" tabindex="61" data-l10n-id="document_properties">
	 <span data-l10n-id="document_properties_label">Document Propertiesâ€¦</span>
	 </button>
	 </div>
	 </div>  <!-- secondaryToolbar -->

	 */
	private void createSecondaryToolbar(Document document, DivElement parent) {
		secondaryToolbar = createChildDiv(document, parent, "secondaryToolbar");
		secondaryToolbar.addClassName("hidden");
		secondaryToolbar.addClassName("doorHangerRight");

		//secondaryToolbarButtonContainer is missing !!!!!!!!!!!!!

		secondaryPresentationMode = createChildButton(document, secondaryToolbar, "secondaryPresentationMode");
		secondaryPresentationMode.addClassName("secondaryToolbarButton presentationMode");
		secondaryPresentationMode.setTitle("Switch to Presentation Mode");
		secondaryPresentationMode.setTabIndex(51);
		secondaryPresentationMode.setAttribute("data-l10n-id", "presentation_mode");
		SpanElement secondaryPresentationModeChildSpan = createChildSpan(document, secondaryPresentationMode, "");
		secondaryPresentationModeChildSpan.setInnerText("Presentation Mode");
		secondaryPresentationModeChildSpan.setAttribute("data-l10n-id", "presentation_mode_label");

		secondaryPrint = createChildButton(document, secondaryToolbar, "secondaryPrint");
		secondaryPrint.addClassName("secondaryToolbarButton print");
		secondaryPrint.setTitle("Print");
		secondaryPrint.setTabIndex(53);
		secondaryPrint.setAttribute("data-l10n-id", "print");
		SpanElement secondaryPrintChildSpan = createChildSpan(document, secondaryPrint, "");
		secondaryPrintChildSpan.setInnerText("Print");
		secondaryPrintChildSpan.setAttribute("data-l10n-id", "print_label");

		secondaryDownload = createChildButton(document, secondaryToolbar, "secondaryDownload");
		secondaryDownload.addClassName("secondaryToolbarButton download");
		secondaryDownload.setTitle("Download");
		secondaryDownload.setTabIndex(54);
		secondaryDownload.setAttribute("data-l10n-id", "download");
		SpanElement secondaryDownloadChildSpan = createChildSpan(document, secondaryDownload, "");
		secondaryDownloadChildSpan.setInnerText("Download");
		secondaryDownloadChildSpan.setAttribute("data-l10n-id", "download_label");

		createChildDiv(document, secondaryToolbar, "horizontalToolbarSeparator");

		firstPage = createChildButton(document, secondaryToolbar, "firstPage");
		firstPage.addClassName("secondaryToolbarButton");
		firstPage.setTitle("Go to First Page");
		firstPage.setTabIndex(56);
		firstPage.setAttribute("data-l10n-id", "first_page");
		SpanElement firstPageChildSpan = createChildSpan(document, firstPage, "");
		firstPageChildSpan.setInnerText("Go to First Page");
		firstPageChildSpan.setAttribute("data-l10n-id", "first_page_label");

		lastPage = createChildButton(document, secondaryToolbar, "lastPage");
		lastPage.addClassName("secondaryToolbarButton");
		lastPage.setTitle("Go to Last Page");
		lastPage.setTabIndex(57);
		lastPage.setAttribute("data-l10n-id", "last_page");
		SpanElement lastPageChildSpan = createChildSpan(document, lastPage, "");
		lastPageChildSpan.setInnerText("Go to Last Page");
		lastPageChildSpan.setAttribute("data-l10n-id", "last_page_label");

		createChildDiv(document, secondaryToolbar, "horizontalToolbarSeparator");

		pageRotateCw = createChildButton(document, secondaryToolbar, "rotateCw");
		pageRotateCw.addClassName("secondaryToolbarButton");
		pageRotateCw.setTitle("Rotate Clockwise");
		pageRotateCw.setTabIndex(58);
		pageRotateCw.setAttribute("data-l10n-id", "page_rotate_cw");
		SpanElement pageRotateCwChildSpan = createChildSpan(document, pageRotateCw, "");
		pageRotateCwChildSpan.setInnerText("Rotate Clockwise");
		pageRotateCwChildSpan.setAttribute("data-l10n-id", "page_rotate_cw_label");

		pageRotateCcw = createChildButton(document, secondaryToolbar, "rotateCcw");
		pageRotateCcw.addClassName("secondaryToolbarButton");
		pageRotateCcw.setTitle("Rotate Counterclockwise");
		pageRotateCcw.setAttribute("data-l10n-id", "page_rotate_ccw");
		pageRotateCcw.setTabIndex(59);
		SpanElement pageRotateCcwChildSpan = createChildSpan(document, pageRotateCcw, "");
		pageRotateCcwChildSpan.setInnerText("Rotate Counterclockwise");
		pageRotateCcwChildSpan.setAttribute("data-l10n-id", "page_rotate_ccw_label");

		createChildDiv(document, secondaryToolbar, "horizontalToolbarSeparator");

		toggleHandTool = createChildButton(document, secondaryToolbar, "handTool");
		toggleHandTool.addClassName("secondaryToolbarButton");
		toggleHandTool.setTitle("Enable hand tool");
		toggleHandTool.setTabIndex(60);
		SpanElement toggleHandToolChildSpan = createChildSpan(document, toggleHandTool, "");
		toggleHandToolChildSpan.setInnerText("Enable hand tool");

		createChildDiv(document, secondaryToolbar, "horizontalToolbarSeparator");

		documentProperties = createChildButton(document, secondaryToolbar, "documentProperties");
		documentProperties.addClassName("secondaryToolbarButton");
		documentProperties.setTitle("Document Properties");
		documentProperties.setTabIndex(61);
		documentProperties.setAttribute("data-l10n-id", "document_properties");
		SpanElement documentPropertiesChildSpan = createChildSpan(document, documentProperties, "");
		documentPropertiesChildSpan.setInnerText("Document Properties");
		documentPropertiesChildSpan.setAttribute("data-l10n-id", "document_properties_label");

	}

	/**
	 <div class="findbar hidden doorHanger" id="findbar">
	 <div id="findbarInputContainer">
	 <input id="findInput" class="toolbarField" title="Find" placeholder="Find in documentâ€¦" tabindex="91" data-l10n-id="find_input">
	 <div class="splitToolbarButton">
	 <button id="findPrevious" class="toolbarButton findPrevious" title="Find the previous occurrence of the phrase" tabindex="92" data-l10n-id="find_previous">
	 <span data-l10n-id="find_previous_label">Previous</span>
	 </button>
	 <div class="splitToolbarButtonSeparator"></div>
	 <button id="findNext" class="toolbarButton findNext" title="Find the next occurrence of the phrase" tabindex="93" data-l10n-id="find_next">
	 <span data-l10n-id="find_next_label">Next</span>
	 </button>
	 </div>
	 </div>

	 <div id="findbarOptionsContainer">
	 <input type="checkbox" id="findHighlightAll" class="toolbarField" tabindex="94">
	 <label for="findHighlightAll" class="toolbarLabel" data-l10n-id="find_highlight">Highlight all</label>
	 <input type="checkbox" id="findMatchCase" class="toolbarField" tabindex="95">
	 <label for="findMatchCase" class="toolbarLabel" data-l10n-id="find_match_case_label">Match case</label>
	 <span id="findResultsCount" class="toolbarLabel hidden"></span>
	 </div>

	 <div id="findbarMessageContainer">
	 <span id="findMsg" class="toolbarLabel"></span>
	 </div>
	 </div>  <!-- findbar -->
	 */
	private void createFindBar(Document document, DivElement parent) {
		findbar = createChildDiv(document, parent, "findbar");
		findbar.addClassName("hidden");
		findbar.addClassName("doorHanger");

		// basics - input for text, previous next
		findbarInputContainer = createChildDiv(document, findbar, "findbarInputContainer");
		findInput = createChildInput(document, findbarInputContainer, "findInput");
		findInput.addClassName("toolbarField");
		findInput.setTitle("Find");
		findInput.setPropertyString("placeholder", "Find in document");
		findInput.setTabIndex(91);
		findInput.setAttribute("data-l10n-id", "find_input");

		DivElement splitToolbarButton = createChildDiv(document, findbarInputContainer, "splitToolbarButton");
		findPrevious = createChildButton(document, splitToolbarButton, "findPrevious");
		findPrevious.addClassName("toolbarButton");
		findPrevious.setTitle("Find the previous occurrence of the phrase");
		findPrevious.setTabIndex(92);
		findPrevious.setAttribute("data-l10n-id","find_previous");

		createChildDiv(document, splitToolbarButton, "splitToolbarButtonSeparator");
		findNext = createChildButton(document, splitToolbarButton, "findNext");
		findNext.addClassName("toolbarButton");
		findNext.setTitle("Find the next occurrence of the phrase");
		findNext.setTabIndex(93);
		findNext.setAttribute("data-l10n-id","find_next");

		//additional options - highlight all, match case
		findbarOptionsContainer = createChildDiv(document, findbar, "findbarOptionsContainer");

		findHighlightAll = createChildCheckbox(document, findbarOptionsContainer, "findHighlightAll");
		findHighlightAll.addClassName("toolbarField");
		findHighlightAll.setTabIndex(94);
		findHighlightAll.setId(Document.get().createUniqueId());

		findHighlightAllLabel = createChildLabel(document, findbarOptionsContainer, "findHighlightAllLabel");
		findHighlightAllLabel.addClassName("toolbarLabel");
		findHighlightAllLabel.setHtmlFor(findHighlightAll.getId());
		findHighlightAllLabel.setInnerText("Highlight all");
		findHighlightAllLabel.setAttribute("data-l10n-id", "find_highlight");

		findMatchCase = createChildCheckbox(document, findbarOptionsContainer, "findMatchCase");
		findMatchCase.addClassName("toolbarField");
		findMatchCase.setTabIndex(95);
		findMatchCase.setId(Document.get().createUniqueId());

		findMatchCaseLabel = createChildLabel(document, findbarOptionsContainer, "findMatchCaseLabel");
		findMatchCaseLabel.addClassName("toolbarLabel");
		findMatchCaseLabel.setHtmlFor(findMatchCase.getId());
		findMatchCaseLabel.setInnerText("Match case");
		findMatchCaseLabel.setAttribute("data-l10n-id", "find_match_case_label");

		findResultsCount = createChildSpan(document, findbarOptionsContainer, "findResultsCount");
		findResultsCount.addClassName("toolbarLabel");
		findResultsCount.addClassName("hidden");

		//messages options - searchar talks back
		findbarMessageContainer = createChildDiv(document, findbar, "findbarMessageContainer");
		findMsg = createChildSpan(document, findbarMessageContainer, "findMsg");
		findMsg.addClassName("toolbarLabel");
	}

	/**
	 * Left sidebar with thumbnail, outline and attachments.

	 <div id="sidebarContainer">
	 <div id="toolbarSidebar">
	 <div class="splitToolbarButton toggled">
	 <button id="viewThumbnail" class="toolbarButton toggled" title="Show Thumbnails" tabindex="2" data-l10n-id="thumbs">
	 <span data-l10n-id="thumbs_label">Thumbnails</span>
	 </button>
	 <button id="viewOutline" class="toolbarButton" title="Show Document Outline (double-click to expand/collapse all items)" tabindex="3" data-l10n-id="document_outline">
	 <span data-l10n-id="document_outline_label">Document Outline</span>
	 </button>
	 <button id="viewAttachments" class="toolbarButton" title="Show Attachments" tabindex="4" data-l10n-id="attachments">
	 <span data-l10n-id="attachments_label">Attachments</span>
	 </button>
	 </div>
	 </div>
	 <div id="sidebarContent">
	 <div id="thumbnailView">
	 </div>
	 <div id="outlineView" class="hidden">
	 </div>
	 <div id="attachmentsView" class="hidden">
	 </div>
	 </div>
	 </div>  <!-- sidebarContainer -->
	 *
	 */
	private void createSidebar(Document document, DivElement parent) {
		sidebarContainer = createChildDiv(document, parent, "sidebarContainer");
		// create sidebar toolbar
		toolbarSidebar = createChildDiv(document, sidebarContainer, "toolbarSidebar");
		DivElement splitToolbarButton = createChildDiv(document, toolbarSidebar, "splitToolbarButton");
		splitToolbarButton.addClassName("toggled");
		viewThumbnail = createChildButton(document, splitToolbarButton, "viewThumbnail");
		viewThumbnail.setTabIndex(2);
		viewThumbnail.addClassName("toolbarButton");
		viewThumbnail.setTitle("Show Thumbnails");
		viewThumbnail.setAttribute("data-l10n-id", "thumbs");

		viewOutline = createChildButton(document, splitToolbarButton, "viewOutline");
		viewOutline.setTabIndex(3);
		viewOutline.addClassName("toolbarButton");
		viewOutline.setTitle("Show Document Outline (double-click to expand/collapse all items)");
		viewOutline.setAttribute("data-l10n-id", "document_outline");

		viewAttachments = createChildButton(document, splitToolbarButton, "viewAttachments");
		viewAttachments.setTabIndex(4);
		viewAttachments.addClassName("toolbarButton");
		viewAttachments.setTitle("Show Attachments");
		viewAttachments.setAttribute("data-l10n-id", "attachments");

		// create sidebar content
		sidebarContent = createChildDiv(document, sidebarContainer, "sidebarContent");
		thumbnailView = createChildDiv(document, sidebarContent, "thumbnailView");
		outlineView = createChildDiv(document, sidebarContent, "outlineView");
		outlineView.addClassName("hidden");
		attachmentsView = createChildDiv(document, sidebarContent, "attachmentsView");
		attachmentsView.addClassName("hidden");
	}

	private void createMainToolbar(Document document, DivElement mainContainer) {
		toolbar = createChildDiv(document, mainContainer, "toolbar");
		toolbarContainer = createChildDiv(document, toolbar, "toolbarContainer");
		toolbarViewer = createChildDiv(document, toolbarContainer, "toolbarViewer");
		toolbarViewerLeft = createChildDiv(document, toolbarViewer, "toolbarViewerLeft");

		sidebarToggleBtn = createChildButton(document, toolbarViewerLeft, "sidebarToggle");
		sidebarToggleBtn.addClassName("toolbarButton");
		sidebarToggleBtn.setTabIndex(11);
		sidebarToggleBtn.setAttribute("data-l10n-id", "toggle_sidebar");

		toolbarButtonSpacer = createChildDiv(document, toolbarViewerLeft, "toolbarButtonSpacer");

		viewFind = createChildDiv(document, toolbarViewerLeft, "viewFind");
		viewFind.addClassName("toolbarButton");
		viewFind.setTabIndex(12);
		viewFind.setTitle("Find in Document");
		viewFind.setAttribute("data-l10n-id", "findbar");

		//paging - previous, next, current page number, jump and all pages
		createPagingSection(document, toolbarViewerLeft);

		toolbarViewerMiddle = createChildDiv(document, toolbarViewer, "toolbarViewerMiddle");
		createZoomingSection(document, toolbarViewerMiddle);

		createRightToolbar(document);

		secondaryToolbarToggle = createChildButton(document, toolbarViewerRight, "secondaryToolbarToggle");
		secondaryToolbarToggle.addClassName("toolbarButton");
		secondaryToolbarToggle.setTitle("Tools");
		secondaryToolbarToggle.setTabIndex(36);
		secondaryToolbarToggle.setAttribute("data-l10n-id", "tools");
	}

	private void createRightToolbar(Document document) {
		toolbarViewerRight = createChildDiv(document, toolbarViewer, "toolbarViewerRight");

		presentationModeBtn = createChildButton(document, toolbarViewerRight, "presentationMode");
		presentationModeBtn.addClassName("toolbarButton hiddenLargeView");
		presentationModeBtn.setTitle("Switch to Presentation Mode");
		presentationModeBtn.setTabIndex(31);
		presentationModeBtn.setAttribute("data-l10n-id", "presentation_mode");

		DivElement verticalToolbarSeparator = createChildDiv(document, toolbarViewerRight, "verticalToolbarSeparator");
		verticalToolbarSeparator.addClassName("hiddenSmallView");
	}

	/**
	 <div class="splitToolbarButton">
	 <button id="zoomOut" class="toolbarButton zoomOut" title="Zoom Out" tabindex="21" data-l10n-id="zoom_out">
	 <span data-l10n-id="zoom_out_label">Zoom Out</span>
	 </button>
	 <div class="splitToolbarButtonSeparator"></div>
	 <button id="zoomIn" class="toolbarButton zoomIn" title="Zoom In" tabindex="22" data-l10n-id="zoom_in">
	 <span data-l10n-id="zoom_in_label">Zoom In</span>
	 </button>
	 </div>
	 <span id="scaleSelectContainer" class="dropdownToolbarButton">
	 <select id="scaleSelect" title="Zoom" tabindex="23" data-l10n-id="zoom">
	 <option id="pageAutoOption" title="" value="auto" selected="selected" data-l10n-id="page_scale_auto">Automatic Zoom</option>
	 <option id="pageActualOption" title="" value="page-actual" data-l10n-id="page_scale_actual">Actual Size</option>
	 <option id="pageFitOption" title="" value="page-fit" data-l10n-id="page_scale_fit">Fit Page</option>
	 <option id="pageWidthOption" title="" value="page-width" data-l10n-id="page_scale_width">Full Width</option>
	 <option id="customScaleOption" title="" value="custom" disabled="disabled" hidden="true"></option>
	 <option title="" value="0.5" data-l10n-id="page_scale_percent" data-l10n-args='{ "scale": 50 }'>50%</option>
	 <option title="" value="0.75" data-l10n-id="page_scale_percent" data-l10n-args='{ "scale": 75 }'>75%</option>
	 <option title="" value="1" data-l10n-id="page_scale_percent" data-l10n-args='{ "scale": 100 }'>100%</option>
	 <option title="" value="1.25" data-l10n-id="page_scale_percent" data-l10n-args='{ "scale": 125 }'>125%</option>
	 <option title="" value="1.5" data-l10n-id="page_scale_percent" data-l10n-args='{ "scale": 150 }'>150%</option>
	 <option title="" value="2" data-l10n-id="page_scale_percent" data-l10n-args='{ "scale": 200 }'>200%</option>
	 <option title="" value="3" data-l10n-id="page_scale_percent" data-l10n-args='{ "scale": 300 }'>300%</option>
	 <option title="" value="4" data-l10n-id="page_scale_percent" data-l10n-args='{ "scale": 400 }'>400%</option>
	 </select>
	 </span>
	 *
	 */

	private void createZoomingSection(Document document, DivElement parent) {
		DivElement splitToolbarButton = createChildDiv(document, parent, "splitToolbarButton");

		zoomOut = createChildButton(document, splitToolbarButton, "zoomOut");
		zoomOut.addClassName("toolbarButton");
		zoomOut.setTabIndex(21);
		zoomOut.setTitle("Zoom Out");
		zoomOut.setAttribute("data-l10n-id","zoom_out");

		createChildDiv(document, splitToolbarButton, "splitToolbarButtonSeparator");

		zoomIn = createChildButton(document, splitToolbarButton, "zoomIn");
		zoomIn.addClassName("toolbarButton");
		zoomIn.setTabIndex(21);
		zoomIn.setTitle("Zoom In");
		zoomIn.setAttribute("data-l10n-id","zoom_in");

		scaleSelectContainer = createChildSpan(document, parent, "scaleSelectContainer");
		scaleSelectContainer.addClassName("dropdownToolbarButton");

		scaleSelect = createChildSelect(document, scaleSelectContainer, "scaleSelect");
		scaleSelect.setTitle("Zoom");
		scaleSelect.setTabIndex(23);
		scaleSelect.setAttribute("data-l10n-id", "zoom");

		createChildOption(document, scaleSelect, "auto", "Automatic Zoom", "page_scale_auto");
		createChildOption(document, scaleSelect, "page-actual", "Actual Size", "page_scale_actual");
		createChildOption(document, scaleSelect, "page-fit", "Fit Page", "page_scale_fit");
		createChildOption(document, scaleSelect, "page-width", "Full Width", "page_scale_width");

		//<option id="customScaleOption" title="" value="custom" disabled="disabled" hidden="true"></option>
		customScaleOption = createChildOption(document, scaleSelect, "custom", "", null);
		customScaleOption.setDisabled(true);
		customScaleOption.setAttribute("hidden", "true");

		createChildOption(document, scaleSelect, "0.5", "50%", null);
		createChildOption(document, scaleSelect, "0.75", "75%", null);
		createChildOption(document, scaleSelect, "1", "100%", null);
		createChildOption(document, scaleSelect, "1.25", "125%", null);
		createChildOption(document, scaleSelect, "1.5", "150%", null);
		createChildOption(document, scaleSelect, "2", "200%", null);
		createChildOption(document, scaleSelect, "3", "300%", null);
		createChildOption(document, scaleSelect, "4", "400%", null);
	}

	/**
	 *
	 <div class="splitToolbarButton hiddenSmallView">
	 <button class="toolbarButton pageUp" title="Previous Page" id="previous" tabindex="13" data-l10n-id="previous">
	 <span data-l10n-id="previous_label">Previous</span>
	 </button>
	 <div class="splitToolbarButtonSeparator"></div>
	 <button class="toolbarButton pageDown" title="Next Page" id="next" tabindex="14" data-l10n-id="next">
	 <span data-l10n-id="next_label">Next</span>
	 </button>
	 </div>
	 <input type="number" id="pageNumber" class="toolbarField pageNumber" title="Page" value="1" size="4" min="1" tabindex="15" data-l10n-id="page">
	 <span id="numPages" class="toolbarLabel"></span>
	 *
	 */
	private void createPagingSection(Document document, DivElement parent) {
		DivElement splitToolbarButton = createChildDiv(document, parent, "splitToolbarButton");
		splitToolbarButton.addClassName("hiddenSmallView");

		previousPage = createChildButton(document, splitToolbarButton, "pageUp");
		previousPage.addClassName("toolbarButton");
		previousPage.setTabIndex(13);
		previousPage.setTitle("Previous Page");
		previousPage.setAttribute("data-l10n-id", "previous");

		createChildDiv(document, splitToolbarButton, "splitToolbarButtonSeparator");

		nextPage = createChildButton(document, splitToolbarButton, "pageDown");
		nextPage.addClassName("toolbarButton");
		nextPage.setTabIndex(14);
		nextPage.setTitle("Next Page");
		nextPage.setAttribute("data-l10n-id", "next");

		pageNumber = createChildNumber(document, parent, "pageNumber");
		pageNumber.addClassName("toolbarField");
		pageNumber.setTitle("Page");
		pageNumber.setValue("1");
		pageNumber.setSize(4);
		pageNumber.setAttribute("min", "1");
		pageNumber.setTabIndex(15);
		pageNumber.setAttribute("data-l10n-id","page");

		numPages = createChildSpan(document, parent, "numPages");
		numPages.addClassName("toolbarLabel");
	}

	private void createViewerContainer(Document document, DivElement mainContainer) {
		viewerContainer = createChildDiv(document, mainContainer, "viewerContainer");
		viewerContainer.setTabIndex(0); // keyboard scrolling does not work without this tabindex 

		viewer = createChildDiv(document, viewerContainer, "viewer");
		viewer.addClassName("pdfViewer");

		overlayContainer = createChildDiv(document, outerContainer, "overlayContainer");
		overlayContainer.addClassName("hidden");

		passwordOverlay = createChildDiv(document, overlayContainer, "passwordOverlay");
		passwordOverlay.addClassName("container");
		passwordOverlay.addClassName("hidden");

		createDocumentPropertiesOverlay(document, overlayContainer);
		createPrintServiceOverlay(document, overlayContainer);
	}

	private void createPrintServiceOverlay(Document document, DivElement parent) {
		printServiceOverlay = createChildDiv(document, parent, "printServiceOverlay");
		printServiceOverlay.addClassName("container");
		printServiceOverlay.addClassName("hidden");

		DivElement dialog = createChildDiv(document, printServiceOverlay, "dialog");
		DivElement row1 = createChildDiv(document, dialog, "row");
		SpanElement message = createChildSpan(document, row1, "");
		message.setInnerText("Preparing document for printing!");

		DivElement row2 = createChildDiv(document, dialog, "row");
		createChildProgress(document, row2, 0, 100);
		SpanElement relativeProgressSpan = createChildSpan(document, row2, "relative-progress");
		relativeProgressSpan.setInnerText("0%");

		DivElement row3 = createChildDiv(document, dialog, "row");
		printCancel = createChildButton(document, row3, "printCancel");
		printCancel.addClassName("overlayButton");
		SpanElement printCancelMessage = createChildSpan(document, printCancel, "");
		printCancelMessage.setInnerText("Cancel");
	}

	private void createDocumentPropertiesOverlay(Document document, DivElement parent) {
		documentPropertiesOverlay = createChildDiv(document, parent, "documentPropertiesOverlay");
		documentPropertiesOverlay.addClassName("container");
		documentPropertiesOverlay.addClassName("hidden");

		DivElement dialog = createChildDiv(document, documentPropertiesOverlay, "dialog");
		fileNameField = addDocumentPropertiesRow(document, dialog, "File name:", "document_properties_file_name");
		fileSizeField = addDocumentPropertiesRow(document, dialog, "File size:", "document_properties_file_size");

		createChildDiv(document, dialog, "separator");

		titleField = addDocumentPropertiesRow(document, dialog, "Title:", "document_properties_title");
		authorField = addDocumentPropertiesRow(document, dialog, "Author:", "document_properties_author");
		subjectField = addDocumentPropertiesRow(document, dialog, "Subject:", "document_properties_subject");
		keywordsField = addDocumentPropertiesRow(document, dialog, "Keywords:", "document_properties_keywords");
		creationDateField = addDocumentPropertiesRow(document, dialog, "Creation Date:", "document_properties_creation_date");
		modificationDateField = addDocumentPropertiesRow(document, dialog, "Modification Date:", "document_properties_modification_date");
		creatorField = addDocumentPropertiesRow(document, dialog, "Creator:", "document_properties_creator");

		createChildDiv(document, dialog, "separator");

		producerField = addDocumentPropertiesRow(document, dialog, "PDF Producer:", "document_properties_producer");
		versionField = addDocumentPropertiesRow(document, dialog, "PDF Version:", "document_properties_version");
		pageCountField = addDocumentPropertiesRow(document, dialog, "Page Count:", "document_properties_page_count");

		DivElement buttonRow = createChildDiv(document, dialog, "buttonRow");
		documentPropertiesClose = createChildButton(document, buttonRow, "documentPropertiesClose");
		documentPropertiesClose.addClassName("overlayButton");
		documentPropertiesClose.setInnerText("Close");
		documentPropertiesClose.setAttribute("data-l10n-id", "document_properties_close");

	}

	private ParagraphElement addDocumentPropertiesRow(Document document, DivElement dialog, String label,
													  String locId) {
		DivElement row = createChildDiv(document, dialog, "row");

		SpanElement span = createChildSpan(document, row, "");
		span.setInnerText(label);
		span.setAttribute("data-l10n-id", locId);

		ParagraphElement result = createChildP(document, row);
		result.setInnerText("-");
		return result;
	}

	private DivElement createDiv_withId(Document document, String nameId) {
		DivElement result = document.createDivElement();
		result.setId(nameId);
		result.addClassName(nameId);

		return result;
	}

	private DivElement createDiv(Document document, String nameId) {
		DivElement result = document.createDivElement();
		result.addClassName(nameId);

		return result;
	}

	private ButtonElement createButton(Document document, String className) {
		ButtonElement result = document.createPushButtonElement();
		result.addClassName(className);

		return result;
	}

	private InputElement createInput(Document document, String className) {
		InputElement result = document.createTextInputElement();
		result.addClassName(className);

		return result;
	}

	private InputElement createNumber(Document document, String className) {
		InputElement result = createInput(document, className);
		result.setAttribute("type", "number");

		return result;
	}

	private InputElement createCheckbox(Document document, String className) {
		InputElement result = document.createCheckInputElement();
		result.addClassName(className);

		return result;
	}

	private LabelElement createLabel(Document document, String className) {
		LabelElement result = document.createLabelElement();
		result.addClassName(className);

		return result;
	}

	private SpanElement createSpan(Document document, String className) {
		SpanElement result = document.createSpanElement();
		result.addClassName(className);

		return result;
	}

	private Element createProgress(Document document, int value, int max) {
		Element result = document.createElement("progress");
		result.setAttribute("value", String.valueOf(value));
		result.setAttribute("max", String.valueOf(max));

		return result;
	}

	private ParagraphElement createP(Document document) {
		ParagraphElement result = document.createPElement();
		return result;
	}

	private SelectElement createSelect(Document document, String className) {
		SelectElement result = document.createSelectElement();
		result.addClassName(className);

		return result;
	}

	private DivElement createChildDiv_withId(Document document, Element parent, String nameId) {
		DivElement result = createDiv_withId(document, nameId);
		parent.appendChild(result);

		return result;
	}

	private DivElement createChildDiv(Document document, Element parent, String nameId) {
		return createChildDiv(document, parent, nameId, true);
	}

	private DivElement createChildDiv(Document document, Element parent, String nameId, boolean append) {
		DivElement result = createDiv(document, nameId);
		if (append)
			parent.appendChild(result);

		return result;
	}

	private ButtonElement createChildButton(Document document, Element parent, String nameId) {
		ButtonElement result = createButton(document, nameId);
		parent.appendChild(result);

		return result;
	}

	private InputElement createChildInput(Document document, Element parent, String nameId) {
		InputElement result = createInput(document, nameId);
		parent.appendChild(result);

		return result;
	}

	private InputElement createChildNumber(Document document, Element parent, String nameId) {
		InputElement result = createNumber(document, nameId);
		parent.appendChild(result);

		return result;
	}

	private InputElement createChildCheckbox(Document document, Element parent, String nameId) {
		InputElement result = createCheckbox(document, nameId);
		parent.appendChild(result);

		return result;
	}

	private LabelElement createChildLabel(Document document, Element parent, String nameId) {
		LabelElement result = createLabel(document, nameId);
		parent.appendChild(result);

		return result;
	}

	private SpanElement createChildSpan(Document document, Element parent, String nameId) {
		SpanElement result = createSpan(document, nameId);
		parent.appendChild(result);

		return result;
	}

	private Element createChildProgress(Document document, Element parent, int value, int max) {
		Element result = createProgress(document, value, max);
		parent.appendChild(result);

		return result;
	}

	private ParagraphElement createChildP(Document document, Element parent) {
		ParagraphElement result = createP(document);
		parent.appendChild(result);

		return result;
	}

	private SelectElement createChildSelect(Document document, Element parent, String nameId) {
		SelectElement result = createSelect(document, nameId);
		parent.appendChild(result);

		return result;
	}

	private OptionElement createChildOption(Document document, SelectElement select, String value, String label,
											String locId) {
		OptionElement option = document.createOptionElement();
		option.setValue(value);
		option.setInnerText(label);
		if (locId != null) {
			option.setAttribute("data-l10n-id", locId);
		}

		select.add(option, null);

		return option;
	}

	public native void startWebViewerLoad(WTPdfViewerWidget instance)
		/*-{
			var configuration = {
				appContainer: $wnd.document.body,
				mainContainer: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::viewerContainer,
				viewerContainer: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::viewer,
				eventBus: null,
				toolbar: {
					container: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::toolbarViewer,

					// paging section
					numPages: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::numPages,
					pageNumber: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pageNumber,
					previous: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::previousPage,
					next: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::nextPage,

					// zooming section
					scaleSelectContainer: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::scaleSelectContainer,
					scaleSelect: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::scaleSelect,
					customScaleOption: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::customScaleOption,
					zoomIn: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::zoomIn,
					zoomOut: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::zoomOut,
					presentationModeButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::presentationModeBtn
				},
				secondaryToolbar: {
					toggleButton:  this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::secondaryToolbarToggle,
					toolbar: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::secondaryToolbar,
					presentationModeButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::secondaryPresentationMode,
					printButton:  this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::secondaryPrint,
					downloadButton:  this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::secondaryDownload,

					firstPageButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::firstPage,
					lastPageButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::lastPage,
					pageRotateCwButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pageRotateCw,
					pageRotateCcwButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pageRotateCcw,
					toggleHandToolButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::toggleHandTool,
					documentPropertiesButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::documentProperties,
				},
				fullscreen: {},
				sidebar: { // this is visible on the left if the page outline is open
					mainContainer: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::mainContainer,
					outerContainer: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::outerContainer,

					// turn on and off sidebar
					toggleButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::sidebarToggleBtn,

					// sidebar content
					thumbnailView: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::thumbnailView,
					outlineView: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::outlineView,
					attachmentsView: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::attachmentsView,

					// sidebar toolbar buttons (e.g. toolbar visible inside the sidebar)
					thumbnailButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::viewThumbnail,
					outlineButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::viewOutline,
					attachmentsButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::viewAttachments,
				},
				findBar: {
					// button to turn on and off search bar
					toggleButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::viewFind,

					// the search bar itself
					bar: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::findbar,
					findField: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::findInput,
					findPreviousButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::findPrevious,
					findNextButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::findNext,
					highlightAllCheckbox: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::findHighlightAll,
					caseSensitiveCheckbox: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::findMatchCase,
					findResultsCount: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::findResultsCount,
					findMsg: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::findMsg,
					findStatusIcon: null, // web viewer does not have this either
				},
				passwordOverlay: {},
				documentProperties: {
					overlayName: 'documentPropertiesOverlay',
					container: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::documentPropertiesOverlay,
					closeButton: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::documentPropertiesClose,
					fields: {
						'fileName': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::fileNameField,
						'fileSize': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::fileSizeField,
						'title': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::titleField,
						'author': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::authorField,
						'subject': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::subjectField,
						'keywords': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::keywordsField,
						'creationDate': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::creationDateField,
						'modificationDate': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::modificationDateField,
						'creator': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::creatorField,
						'producer': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::producerField,
						'version': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::versionField,
						'pageCount': this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pageCountField,
					}
				},
				printing: {
					printServiceOverlay: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::printServiceOverlay,
					printCancel: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::printCancel,
				},
				errorWrapper: {},
				progressBar: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::progress,
				progressBarOwner: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::root,
				printContainer: this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::printContainer,

				// note that openFileInputName field is not used anymore, both menu item and event handler were removed
				openFileInputName: 'fileInput',
				debuggerScriptPath: './debugger.js',
				defaultUrl: '',
				workerSrc: 'APP/PUBLISHED/pdf.worker.js',
			};

			var zero = $wnd.lsps.widgets.wtPdfViewerFactory();
			this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication = zero.webViewerLoad(configuration);
		}-*/;

	public void setResourceFile(String fileName, String password) {
		this.fileName = fileName;
		this.password = password;
		loadResourcePdf(fileName, password);
	}

	public void setErrorListener(PdfViewerWidgetErrorListener errorListener) {
		this.errorListener = errorListener;
	}

	public void onClientSideError(String error) {
		//getRpcProxy(TriStateCheckBoxV8ServerRpc.class).setValue(value);
		if (errorListener!=null)
			errorListener.onError(error);
	}

	public native void loadResourcePdf(String fileName, String password)
		/*-{
			var fileName = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::fileName;
			if (!fileName) {
				return ;
			}
			var self = this;
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			var args = password ? {password: password} : undefined;
			pdfApplication.webViewerOpenFileViaURL(fileName, args, function(error) {
				self.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::onClientSideError(Ljava/lang/String;)(error == null ? "null" : error.message);
			});
			pdfApplication.webViewerFirstPage();
		}-*/;

	public native void firstPage()
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerFirstPage();
		}-*/;

	public native void lastPage()
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerLastPage();
		}-*/;

	public native void previousPage()
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerPreviousPage();
		}-*/;

	public native void nextPage()
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerNextPage();
		}-*/;

	public native void setPage(int page)
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerPageNumberChanged({value: page});
		}-*/;

	public native void setShowPreviousViewOnLoad(boolean showPreviousViewOnLoad)
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerPreferences({showPreviousViewOnLoad: showPreviousViewOnLoad});
		}-*/;

	public native void download()
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerDownload();
		}-*/;

	public native void print()
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerPrint();
		}-*/;

	public native void toggleHandTool()
		/*-{
			var pdfApplication = this.@ru.sms.addons.pdfviewer.web.toolkit.ui.client.addons.wtpdfviewer.WTPdfViewerWidget::pdfApplication;
			pdfApplication.webViewerHandToolToggle();
		}-*/;

	@FunctionalInterface
	public interface PdfViewerWidgetErrorListener {
		public void onError(String error);
	}
}

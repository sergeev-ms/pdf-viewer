# PDF Viewer addon
CUBA add-on based on Vaadin add-on [WT PDF Viewer](https://github.com/WhitesteinTechnologies/wt-pdf-viewer)

| Platform Version | Add-on Version |
| ---------------- | -------------- |
| 7.2.9            | 0.3.5          |
| 7.2.7            | 0.3.3          |
| 7.2.6            | 0.3.2          |
| 7.2.5            | 0.3.1          |
| 7.2.3            | 0.1.1          |
| 7.2.x            | 0.1            |


Add custom application component to your project:

* Artifact group: `ru.sms.addons.pdfviewer`
* Artifact name: `pdf-global`
* Version: *add-on version*

For pdf-viewer localization `CubaBootstrapListener` was replaced with `CustomBootstrapListener`. 
So if you replace `CubaBootstrapListener` in your base project you have to reimplement resource appending
from the add-on's `CustomBootstrapListener`.

The locale of pdf-viewer depends on browser locale, not on cuba application. Includes only `en-us` and `ru` locales.

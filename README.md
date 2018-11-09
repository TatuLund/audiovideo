# GwtVideo Add-on for Vaadin 8

AudioVideo is a UI component add-on for Vaadin 8. It consists of GwtAudio and GwtVideo components which are replacements
for Vaadin Audio and Video components with more API features and events.

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to https://vaadin.com/directory/component/audiovideo

## Building and running demo

git clone http://github.com/tatulund/audiovideo
mvn clean install
cd demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- JRebel Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine. 

### Debugging server-side

If you have not already compiled the widgetset, do it now by running vaadin:install Maven target for gwtav-root project.

If you have a JRebel license, it makes on the fly code changes faster. Just add JRebel nature to your gwtav-demo project by clicking project with right mouse button and choosing JRebel > Add JRebel Nature

To debug project and make code modifications on the fly in the server-side, right-click the gwtav-demo project and choose Debug As > Debug on Server. Navigate to http://localhost:8080/gwtav-demo/ to see the application.

### Debugging client-side

Debugging client side code in the gwtav-demo project:
  - run "mvn vaadin:run-codeserver" on a separate console while the application is running
  - activate Super Dev Mode in the debug window of the application or by adding ?superdevmode to the URL
  - You can access Java-sources and set breakpoints inside Chrome if you enable source maps from inspector settings.
 
## Release notes

### Version 1.2.0
- Add support for MediaSeekedEvent
- Report position to server always when new position is set by user
- Small bugfixes in range request support

### Version 1.1.0
- Add support for setPosition(..) to work correctly with ConnectorResources, see issue https://github.com/vaadin/framework/issues/1742 and https://github.com/vaadin/framework/issues/4266
- Fixed stop() and pause() to report correct position
- Code refactoring

### Version 1.0
- Initial release
- GwtAudio, improved Audio component with additional API and events
- GwtVideo, improved Video component with additional API and events

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. That said, I encourage you to report issues and improvement ideas to issue tracker.

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

Major pieces of development of this add-on has been sponsored by Support and Prime customers of Vaadin. See vaadin.com/support and Development on Demand for more details.

AudioVideo is written by Tatu Lund

# Developer Guide

## Features

### GwtAudio

Improved replacement for framework Audio component with additional API and Events

### GwtVideo

Improved replacement for framework Video component with additional API and Events

## API

GwtVideo JavaDoc is available online at https://vaadin.com/directory/component/audiovideo/api

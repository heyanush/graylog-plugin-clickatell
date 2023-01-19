# Clickatell SMS Plugin for Graylog

An alarm callback plugin for integrating the Clickatell SMS API into Graylog. 

This plugin is a fork of [graylog-plugin-sample](https://github.com/Graylog2/graylog-plugin-sample) and is inspired from [graylog-plugin-twiliosms](https://github.com/graylog-labs/graylog-plugin-twiliosms).


Installation
------------

[Download the plugin](https://github.com/heyanush/graylog-plugin-clickatell/releases)
and place the `.jar` file in your Graylog plugin directory. The plugin directory
is the `plugins/` folder relative from your `graylog-server` directory by default
and can be configured in your `graylog.conf` file.

Restart `graylog-server` and you are done.

Development
-----------

This project is using Maven 3 and requires Java 8 or higher.

You can build the plugin (JAR) with `mvn package`.

DEB and RPM packages can be built with `mvn jdeb:jdeb` and `mvn rpm:rpm` respectively.

Getting started
---------------

This project is using Maven 3 and requires Java 8 or higher.

* Clone this repository.
* Run `mvn package` to build a JAR file.
* Optional: Run `mvn jdeb:jdeb` and `mvn rpm:rpm` to create a DEB and RPM package respectively.
* Copy generated JAR file in target directory to your Graylog plugin directory.
* Restart the Graylog.

Resources
---------------
https://github.com/graylog-labs/graylog-plugin-twiliosms

https://bitbucket.org/proximus/smseagle-graylog/src/master/

https://github.com/Graylog2/graylog-plugin-sample

https://github.com/Graylog2/graylog-plugin-integrations

https://github.com/graylog-labs/graylog-plugin-slack

https://github.com/Graylog2/docker-compose

<!--
This file is part of liferay-clojure-integration.

liferay-clojure-integration is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

liferay-clojure-integration is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
more details.

You should have received a copy of the GNU General Public License along with
liferay-clojure-integration. If not, see <http://www.gnu.org/licenses/>.
-->
Introduction
============

This module provides a [liferay](http://www.liferay.com) scripting implementation allowing the use of
[clojure](http://www.clojure.org) as a scripting engine (for example through the control panel or via a scheduled
task).

Usage
=====

To build this, you'll first need to get hold of the [permeance-parent](https://github.com/permeance/permeance-parent)
module and have it installed locally.

Until this artifact is hosted somewhere centrally, you'll need to check it out, and install it locally
via the standard "mvn install" command. Something like:

```shell
$ mvn install
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building liferay-clojure-integration 0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 8.619s
[INFO] Finished at: Sun Sep 23 23:13:23 EST 2012
[INFO] Final Memory: 14M/81M
[INFO] ------------------------------------------------------------------------
```

Once that is done, you should deploy the resulting WAR via the standard liferay deployment mechanism:

```shell
$ cp target/*.war ~/Applications/liferay-portal-6.1.10-ee-ga1/deploy/
...
23:20:31,585 INFO  [AutoDeployDir:167] Processing liferay-clojure-integration-0.1-SNAPSHOT.war
23:20:31,588 INFO  [PortletAutoDeployListener:61] Deploying package as a web application
...
23:20:42,399 INFO  [HookHotDeployListener:962] Hook for liferay-clojure-integration-0.1-SNAPSHOT is available for use
23:20:42,401 INFO  [PortletHotDeployListener:614] Registering portlets for liferay-clojure-integration-0.1-SNAPSHOT
23:20:42,484 INFO  [PortletHotDeployListener:433] 1 portlet for liferay-clojure-integration-0.1-SNAPSHOT is available for use
```

Execution
=========

Scripts executed by this plugin do so under their own temporary namespace, with access to two vars:

  + 'input-objects' which is a map of strings to objects which are the inputs supplied to the script by liferay.

  + 'output-names' which is a set of strings which represent the names expected to be returned by the user supplied
    function.

Scripts are expected to return a map of strings to objects (probably keyed by 'output-names' if the return values are
to be meaningful). If a single 'output-name' is required, a single object can be returned.

Example
=======

Running from the control panel with the script:

```clojure
(println *ns*)
(println input-objects)
(println output-names)
```

Produces in liferay's logs (formatted here):

```clojure
#<Namespace sym__6__auto__>
#<HashMap {actionResponse=com.liferay.portlet.ActionResponseImpl@2370a0ad,
           portletContext=com.liferay.portlet.PortletContextImpl@1142bdd,
           userInfo={liferay.company.id=10153,
                     liferay.user.id=10195,
                     user.bdate=Thu Jan 01 00:00:00 EST 1970,
                     user.gender=male,
                     user.employer=,
                     user.department=,
                     user.jobtitle=,
                     user.name.prefix=,
                     user.name.given=Test,
                     user.name.family=Test,
                     user.name.middle=,
                     user.name.suffix=,
                     user.name.nickName=test,
                     user.home-info.postal.name=,
                     user.home-info.postal.street=,
                     user.home-info.postal.city=,
                     user.home-info.postal.stateprov=,
                     user.home-info.postal.postalcode=,
                     user.home-info.postal.country=,
                     user.home-info.postal.organization=,
                     user.home-info.telecom.telephone.intcode=,
                     user.home-info.telecom.telephone.loccode=,
                     user.home-info.telecom.telephone.number=,
                     user.home-info.telecom.telephone.ext=,
                     user.home-info.telecom.telephone.comment=,
                     user.home-info.telecom.fax.intcode=,
                     user.home-info.telecom.fax.loccode=,
                     user.home-info.telecom.fax.number=,
                     user.home-info.telecom.fax.ext=,
                     user.home-info.telecom.fax.comment=,
                     user.home-info.telecom.mobile.intcode=,
                     user.home-info.telecom.mobile.loccode=,
                     user.home-info.telecom.mobile.number=,
                     user.home-info.telecom.mobile.ext=,
                     user.home-info.telecom.mobile.comment=,
                     user.home-info.telecom.pager.intcode=,
                     user.home-info.telecom.pager.loccode=,
                     user.home-info.telecom.pager.number=,
                     user.home-info.telecom.pager.ext=,
                     user.home-info.telecom.pager.comment=,
                     user.home-info.online.email=test@liferay.com,
                     user.home-info.online.uri=,
                     user.business-info.postal.name=,
                     user.business-info.postal.street=,
                     user.business-info.postal.city=,
                     user.business-info.postal.stateprov=,
                     user.business-info.postal.postalcode=,
                     user.business-info.postal.country=,
                     user.business-info.postal.organization=,
                     user.business-info.telecom.telephone.intcode=,
                     user.business-info.telecom.telephone.loccode=,
                     user.business-info.telecom.telephone.number=,
                     user.business-info.telecom.telephone.ext=,
                     user.business-info.telecom.telephone.comment=,
                     user.business-info.telecom.fax.intcode=,
                     user.business-info.telecom.fax.loccode=,
                     user.business-info.telecom.fax.number=,
                     user.business-info.telecom.fax.ext=,
                     user.business-info.telecom.fax.comment=,
                     user.business-info.telecom.mobile.intcode=,
                     user.business-info.telecom.mobile.loccode=,
                     user.business-info.telecom.mobile.number=,
                     user.business-info.telecom.mobile.ext=,
                     user.business-info.telecom.mobile.comment=,
                     user.business-info.telecom.pager.intcode=,
                     user.business-info.telecom.pager.loccode=,
                     user.business-info.telecom.pager.number=,
                     user.business-info.telecom.pager.ext=,
                     user.business-info.telecom.pager.comment=,
                     user.business-info.online.email=test@liferay.com,
                     user.business-info.online.uri=,
                     user.name.random=Ccc},
           actionRequest=com.liferay.portlet.ActionRequestImpl@3744d008,
           out=com.liferay.portal.kernel.io.unsync.UnsyncPrintWriter@6b5d991,
           preferences=com.liferay.portlet.PortletPreferencesWrapper@155871e4,
           portletConfig=com.liferay.portlet.PortletConfigImpl@68515083}>
nil
```

(Note: 'output-names' is always empty when the script is executed from the control panel)

License
=======

This project is licensed under the
[GNU Lesser General Public License, Version 3](http://www.gnu.org/licenses/lgpl-3.0.html)

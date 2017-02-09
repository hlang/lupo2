# LUPO 2

LDAP user management


### Setup Test Environment
1. Install [Apache Directory Studio](https://directory.apache.org/studio/)
2. Start the embedded LDAP Server
3. Add a [new partition](https://directory.apache.org/apacheds/basic-ug/1.4.3-adding-partition.html) `o=sevenSeas` and restart the server
4. Import [LDIF example data](http://directory.apache.org/apacheds/basic-ug/1.5-sample-configuration.html) for the _sailors of the seven seas_

## Development server
Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive/pipe/service/class/module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `-prod` flag for a production build.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).
Before running the tests make sure you are serving the app via `ng serve`.

## Running backend proxy
To proxy the backend, adjust the proxy.conf.json file

Run `ng serve --proxy-config proxy.conf.json`

## Deploying to GitHub Pages

Run `ng github-pages:deploy` to deploy to GitHub Pages.

## Further help

To get more help on the `angular-cli` use `ng help` or go check out the [Angular-CLI README](https://github.com/angular/angular-cli/blob/master/README.md).

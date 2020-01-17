[<img src="https://raw.githubusercontent.com/ForgeRock/forgerock-logo-dev/master/Logo-fr-dev.png" align="right" width="220px"/>](https://developer.forgerock.com/)

| |Current Status|
|---|---|
|License|![license](https://img.shields.io/github/license/ACRA/acra.svg)|

**_This repository is part of the Consumer Data Right toolkit. If you just landed to that repository looking for our tool kit,_
_we recommend having a first read to_ https://github.com/OpenBankingToolkit/openbanking-toolkit**



# Consumer Data Right Reference Implementation - Backend

## How to install the backend development environment

### Building project and docker images

Just run `mvn install`. This will build the images but if you want to skip them, run `mvn install -Ddockerfile.skip`

### Setup the host files

You will need to create some new hostnames for the application.

Ensure your /etc/hosts file looks like [hosts](./hosts). We recommend using Gas mask for managing your hostnames.

### SSL: Adding self-signed CA for dev environment

Follow the steps for all the certificates in the folder `certificates/`.

#### Install certificate in our truststore

1. Click on it and your mac os will show you the system config view.
1. Add it and mark it as trusted, as follow:

![](images/installCA.png?raw=true)

#### Install the CA in your system or browser truststore

The CAs you'll need to add are:
- [obri-internal-ca.crt](keystore/ca/obri-internal-ca.crt)
- [obri-external-ca.crt](keystore/ca/obri-external-ca.crt)
- [OB_SandBox_PP_Issuing_CA.cer](keystore/obOfficialCertificates/OB_SandBox_PP_Issuing_CA.cer)
- [OB_SandBox_PP_Root_CA.cer](keystore/obOfficialCertificates/OB_SandBox_PP_Root_CA.cer)

### Install the unlimited strength file extension

You will need to install this extension as described in https://stackoverflow.com/questions/6481627/java-security-illegal-key-size-or-default-parameters

## How to run the development environment

### Docker

#### System Requirements

If running on docker for mac you'll need to allocate at least 6GB of memory and 3 CPUs

#### Setup
1. If you have not already, register on https://backstage.forgerock.com/
1. Download AM 6.5.1 war file from backstage. This *must* be version 6.5.1.0, both the evaluation and full editions will work.
    https://backstage.forgerock.com/downloads/browse/am/archive/productId:am/minorVersion:6.5/version:6.5.1/releaseType:full
1. Copy the war file to `forgerock-am/_binaries` and rename the file to `am.war`, so that the path is `forgerock-am/_binaries/am.war`.
1. Download the Amster 6.5.1 zip file from backstage
    https://backstage.forgerock.com/downloads/browse/am/archive/productId:amster/minorVersion:6.5/version:6.5.1/releaseType:full
1. Copy the amster zip file to `forgerock-am/_binaries` and rename the file to `amster.zip`, so that the path is `forgerock-am/_binaries/amster.zip`.
1. Run `docker-compose up -d` and wait for services to start

You don't need to run all the microservices all the time. Depending on what you are working on,
you can choose to enable a subset of the microservices.
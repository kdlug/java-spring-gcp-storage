# Google Cloud Java Client for Storage


## Install Cloud SDK

https://cloud.google.com/sdk/docs/

## Generate a JSON service account key.

1. Going to the Google Cloud Platform Console
2. If we haven’t yet defined a GCP project, we click the create button and enter a project name, such as “project-name“
3. Select “new service account” from the drop-down list
4. Add a name such as “account-name” into the account name field.
5. Under “role” select Project, and then Owner in the submenu.
6. Select create, and the console downloads a private key file.

## Define GOOGLE_APPLICATION_CREDENTIALS environment variable

Define the environment variable GOOGLE_APPLICATION_CREDENTIALS to be the location of the key. For example:

```console
export GOOGLE_APPLICATION_CREDENTIALS=/path/to/my/key.json
```

f.ex

```console
export GOOGLE_APPLICATION_CREDENTIALS="/home/$USER/bucket-name.json"

# check
cat $GOOGLE_APPLICATION_CREDENTIALS
```

> Don't use ~ to specify home directory, may not work.

### Idea

In order to set environment variable in IntelliJ IDEA go to `Run > Edit Configurations > Environment Variables` and add new variable.

## Add Google Cloud Storage java client

Add gradle dependency to build.gradle file

```gradle
dependencies {
	implementation 'com.google.cloud:google-cloud-storage:1.76.0'
}
```

## Connecting to storage

If we use GOOGLE_APPLICATION_CREDENTIALS environment variable, we can use the default instance:

```java
Storage storage = StorageOptions.getDefaultInstance().getService();
```

In other way we have to create Credentials instance and pass to storage with the project name.

```java
Credentials credentials = GoogleCredentials
  .fromStream(new FileInputStream("path/to/file"));
Storage storage = StorageOptions.newBuilder().setCredentials(credentials)
  .setProjectId("project-name").build().getService();
```


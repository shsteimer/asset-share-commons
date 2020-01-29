## Cloud Set up

AEM as a Cloud Service Sandbox accounts are provisioned with IMS organisations that only allow Adobe IDs to be added. Adobe Asset Link _requires Enterprise or Federated IDs_ to login to AEM.

To activate Federated IDs in your IMS organisation:

1. Go to [https://adminconsole.adobe.com/](https://adminconsole.adobe.com/)
2. Under __Settings → Identity → Domains__, tap on "Add Domains" and enter `adobe.com` to request access to the domain for your account
3. Wait for your request to get approved (This could day hours to days).
4. Once approved, go to Users and add new users with their `@adobe.com` email address, selecting Federated ID (not Adobe ID)
5. If you are the Administrator of your IMS Organisation, you cannot change your own user to Federated ID. Instead, add another user with full administrative rights and have that user remove your Adobe ID and re-add you with your Federated ID.
6. Once a Federated ID has been added, it has to be assigned to the product context of the AEM as a Cloud Service environment where you want the user to have access to.
7. The user will be able to login from Adobe Asset Link s/he has been added to your IMS organisation with their Federated ID and added to the product context

### Troubleshooting the Authorization Error

The first time logging in to AEM via the Asset Link panel you may see an *Authorization Error*.

![Authorization Error](./adobe-asset-link/images/auth-error.png)

To resolve this:

1. Log in to AEM as `admin` using the local account
2. Navigate to <a href="/libs/granite/security/content/useradmin.html" target="_blank">Tools > Security > User Management</a>
3. Find your user and click to __Edit__
4. Select the **Group** tab
5. Add yourself as a member of the __Administrators__ group
	![Administrator group](./adobe-asset-link/images/administrator-group.png)
6. Save your changes
7. Re-start the Creative Suite product and you should be authorized

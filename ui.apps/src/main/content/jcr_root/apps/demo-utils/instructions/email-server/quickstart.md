## Local Set up

When running AEM as a Cloud Sevice locally via the AEM SDK's Quickstart Jar, Demo Utils installs a Email server configuration that works, because the AEM as a Could Service infrastructure is not in play to block the calls from AEM to the Email Server.

This package installs a SMTP E-mail Server configuration using the e-mail account **aem@enablementadobe.com**.

This SMTP server uses SendGrid and works on or off the Adobe VPN.

Please only use this SMTP Servers credentials for Demo Utils as it is limited to 25,000 e-mails a month.

Remember, AEM cannot send e-mail from the Cloud infrastructure at this time, this ONLY works when running AEM locally using the Quickstart Jar.

package com.simplificacontabil.tomcat;

import java.util.regex.Pattern;

public class RequestBlockPatterns {

    public static final Pattern MALICIOUS_PARAM_PATTERN = Pattern.compile(
            ".*(redirect_uri=|template=|post_logout_redirect_uri=|client_id=|scope=|id=|auth=|channel=|ref=|" +
                    "config=|\\.env|\\.git|sql=|script=|%3Cscript|base64,|eval\\(|php\\().*",
            Pattern.CASE_INSENSITIVE
    );

    public static final Pattern UA_FAKE_OR_OLD_PATTERN = Pattern.compile(
            ".*(curl/|zgrab|assetnote|netcraft|masscan|hydra|nmap|sqlmap|nikto|crawler|" +
                    "Chrome/(\\d{1,2}|1[01][0-9])\\.|" +
                    "Android 4\\.|Android 5\\.|BNTV400|iPhone OS 13_|Mac OS X 10_13_|Knoppix|custom-asynchttpclient|" +
                    "Windows NT 6\\.|Firefox/[1-9][0-9]?\\.|Safari/53\\.|X11; Linux i686|" +
                    "Mozilla/5.0 zgrab|Mozilla/5.0 \\(compatible; NetcraftSurveyAgent).*",
            Pattern.CASE_INSENSITIVE
    );

    public static final Pattern MALICIOUS_PATH_PATTERN = Pattern.compile(
            ".*(\\.(env|git)|/\\.(env|git|aws/credentials|envrc)|docker-compose|/wp-admin|/wp-login|/phpmyadmin|/admin|/cgi-bin|" +
                    "/setup|/install|/robots\\.txt|/sitemap\\.xml|/backup\\.zip|/env\\.backup|/debug\\.log|/phpinfo\\.php|/RDWeb/Pages|" +
                    "/\\.well-known|/config\\.json|/config\\.php|/env\\.bak|/\\.ssh|/\\.aws|/composer\\.json|/package\\.json|" +
                    "/server-status|/id_rsa|/db\\.sql|/backup|/old|/test|/tmp|/logs|/error\\.log|/debug|/core\\.dump|" +
                    "/index\\.php|/\\.DS_Store|/webui/|/geoserver/web/|\\.aspx|/get\\.php" +
                    ").*",
            Pattern.CASE_INSENSITIVE
    );

    public static final Pattern MALICIOUS_USER_AGENT_PATTERN = Pattern.compile(
            ".*(sqlmap|crawler|scan|nmap|nikto|masscan|hydra).*",
            Pattern.CASE_INSENSITIVE
    );
}
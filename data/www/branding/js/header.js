/* -------------------------------------------------------------------------- */
/*                              h e a d e r . j s                             */
/* -------------------------------------------------------------------------- */
document.write("<header>");

// Navigation tabs
document.write("<nav>");
document.write("    <ul class='tabs'>");
insert("Home", "index.html", true);
insert("Snapshots", "docs/manual/snapshots.html", true);
insert("Releases", "docs/manual/releases.html", true);
insert("Handbook", "docs/manual/handbook.html", true);
insert("API", "http://audiveris.kenai.com/docs/api/index.html", false);
document.write("    </ul>");
document.write("</nav>");

// Logo
document.write("<a id='logo' href='" + context.root + "index.html'>");
document.write("   <div id='logo-subtitle'>Open Music Scanner</div>");
document.write("</a>");

// Download button
document.write("<a id='download-button' href='http://kenai.com/projects/audiveris/downloads?field=title&order=desc'>");
document.write("    <div id='download-version'>");
document.write("        <b>V4.2</b>");
document.write("    </div>");
document.write("</a>");

document.write("</header>");

function insert(label, url, relative) {
    document.write("        <li><a");
    if (label === context.page) {
        document.write(" class='current'");
    }
    document.write(" href='");
    if (relative) {
        document.write(context.root);
    }
    document.write(url);
    document.write("'>" + label + "</a></li>");
}

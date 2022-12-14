<#-- @ftlvariable name="version" type="java.lang.String" -->
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <base href="~/"/>
    <!-- Bootstrap 5 responsive CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@300;400;700&display=swap" rel="stylesheet">
    <!-- Styles -->
    <link href="css/site.css" rel="stylesheet"/>
    <title>SmartREST - Java Edition</title>
    <!-- Icons -->
    <link rel="apple-touch-icon" sizes="57x57" href="/apple-icon-57x57.png">
    <link rel="apple-touch-icon" sizes="60x60" href="/apple-icon-60x60.png">
    <link rel="apple-touch-icon" sizes="72x72" href="/apple-icon-72x72.png">
    <link rel="apple-touch-icon" sizes="76x76" href="/apple-icon-76x76.png">
    <link rel="apple-touch-icon" sizes="114x114" href="/apple-icon-114x114.png">
    <link rel="apple-touch-icon" sizes="120x120" href="/apple-icon-120x120.png">
    <link rel="apple-touch-icon" sizes="144x144" href="/apple-icon-144x144.png">
    <link rel="apple-touch-icon" sizes="152x152" href="/apple-icon-152x152.png">
    <link rel="apple-touch-icon" sizes="180x180" href="/apple-icon-180x180.png">
    <link rel="icon" type="image/png" sizes="192x192" href="/android-icon-192x192.png">
    <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="96x96" href="/favicon-96x96.png">
    <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
    <meta name="msapplication-TileColor" content="#ffffff">
    <meta name="msapplication-TileImage" content="/ms-icon-144x144.png">
    <meta name="theme-color" content="#ffffff">
</head>
<body>
    <div class="row">
        <div class="col-sd-1 col-md-2">
            &nbsp;
        </div>
        <div class="col-sd-10 col-md-8">
            <div class="row mt-3 mb-2">
                <div class="col-md-6">
                    &nbsp;
                </div>
                <div class="col-md-6">
                    <img src="/img/springboot.png" class="rounded float-end" width="40px" alt="SpringBoot Logo"/>
                </div>
            </div>
            <div class="p-5 bg-light border rounded-3">
                <h2 class="text-primary">SmartREST - Java Edition</h2>
                <p class="versions">A template project for SpringBoot based REST API - v.<span id="version">${version}</span></p>
                <div class="pt-3 mt-4 mb-5">
                        <a class="btn btn-primary" href="/swagger-ui/index.html" role="button">Documentation</a>
                        &nbsp;
                        <a class="btn btn-success" href="/h2" role="button">H2 DB Console</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sd-1 col-md-2">
            &nbsp;
        </div>
    </div>

    <!-- Bootstrap 5 JS with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
</body>
</html>

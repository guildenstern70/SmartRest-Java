<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${appName} - Java Edition</title>
    <style>
        :root {
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --secondary: #0f172a;
            --success: #16a34a;
            --success-hover: #15803d;
            --info: #0284c7;
            --info-hover: #0369a1;
            --bg: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #1e293b;
            --text-muted: #64748b;
            --border: #e2e8f0;
            --code-bg: #f1f5f9;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
            background-color: var(--bg);
            color: var(--text-main);
            line-height: 1.6;
            padding: 2rem 1rem;
        }

        .container {
            max-width: 1024px;
            margin: 0 auto;
        }

        /* Hero Header */
        header.hero {
            background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
            color: #ffffff;
            border-radius: 16px;
            padding: 3rem 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1);
        }

        .hero-badge {
            display: inline-block;
            background: rgba(37, 99, 235, 0.3);
            border: 1px solid rgba(147, 197, 253, 0.4);
            color: #bfdbfe;
            padding: 0.25rem 0.75rem;
            border-radius: 9999px;
            font-size: 0.85rem;
            font-weight: 600;
            margin-bottom: 1rem;
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .hero h1 {
            font-size: 2.75rem;
            font-weight: 800;
            letter-spacing: -0.025em;
            margin-bottom: 0.75rem;
        }

        .hero p.lead {
            font-size: 1.2rem;
            color: #cbd5e1;
            max-width: 720px;
            margin-bottom: 2rem;
        }

        /* Action Buttons */
        .actions {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.75rem 1.5rem;
            border-radius: 8px;
            font-weight: 600;
            font-size: 0.95rem;
            text-decoration: none;
            cursor: pointer;
            transition: all 0.2s ease-in-out;
            border: none;
        }

        .btn-primary {
            background-color: var(--primary);
            color: #ffffff;
            box-shadow: 0 4px 6px -1px rgba(37, 99, 235, 0.3);
        }

        .btn-primary:hover {
            background-color: var(--primary-hover);
            transform: translateY(-2px);
        }

        .btn-success {
            background-color: var(--success);
            color: #ffffff;
            box-shadow: 0 4px 6px -1px rgba(22, 163, 74, 0.3);
        }

        .btn-success:hover {
            background-color: var(--success-hover);
            transform: translateY(-2px);
        }

        .btn-info {
            background-color: var(--info);
            color: #ffffff;
            box-shadow: 0 4px 6px -1px rgba(2, 132, 199, 0.3);
        }

        .btn-info:hover {
            background-color: var(--info-hover);
            transform: translateY(-2px);
        }

        /* Content Grid */
        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }

        .card {
            background: var(--card-bg);
            border-radius: 12px;
            padding: 1.75rem;
            border: 1px solid var(--border);
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
        }

        .card h2 {
            font-size: 1.35rem;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            color: var(--secondary);
            border-bottom: 2px solid var(--code-bg);
            padding-bottom: 0.5rem;
        }

        .card p {
            color: var(--text-main);
            margin-bottom: 1rem;
        }

        /* Tech Stack Badges */
        .tech-list {
            display: flex;
            flex-wrap: wrap;
            gap: 0.6rem;
            margin-top: 0.5rem;
        }

        .tech-badge {
            background: var(--code-bg);
            border: 1px solid var(--border);
            color: #334155;
            padding: 0.35rem 0.75rem;
            border-radius: 6px;
            font-size: 0.875rem;
            font-weight: 500;
        }

        .tech-badge strong {
            color: var(--primary);
        }

        /* Code snippets and tables */
        code {
            font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
            background-color: var(--code-bg);
            padding: 0.2rem 0.4rem;
            border-radius: 4px;
            font-size: 0.85em;
            color: #d946ef;
        }

        pre {
            background: #0f172a;
            color: #f8fafc;
            padding: 1rem;
            border-radius: 8px;
            overflow-x: auto;
            font-size: 0.875rem;
            margin: 0.5rem 0 1rem;
        }

        ul {
            padding-left: 1.25rem;
            margin-bottom: 1rem;
        }

        li {
            margin-bottom: 0.4rem;
        }

        .meta-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 0.5rem;
            font-size: 0.9rem;
        }

        .meta-table th, .meta-table td {
            text-align: left;
            padding: 0.5rem 0.75rem;
            border-bottom: 1px solid var(--border);
        }

        .meta-table th {
            color: var(--text-muted);
            font-weight: 600;
            width: 35%;
        }

        /* Footer */
        footer {
            text-align: center;
            color: var(--text-muted);
            font-size: 0.875rem;
            padding: 2rem 0 1rem;
            border-top: 1px solid var(--border);
        }

        footer a {
            color: var(--primary);
            text-decoration: none;
        }

        footer a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Hero Section -->
        <header class="hero">
            <span class="hero-badge">Microservice Skeleton</span>
            <h1>${appName}</h1>
            <p class="lead">
                A clean, modern REST API template in Java featuring code-first Hibernate, in-memory H2 database, 
                interactive OpenAPI documentation, health monitoring, and server-side FreeMarker UI.
            </p>
            <div class="actions">
                <a href="${swaggerUrl}" class="btn btn-primary" id="btn-explore-apis" target="_blank" rel="noopener noreferrer">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path></svg>
                    Explore APIs
                </a>
                <a href="${healthUrl}" class="btn btn-success" id="btn-health" target="_blank" rel="noopener noreferrer">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline></svg>
                    Health
                </a>
                <a href="${apiDocsUrl}" class="btn btn-info" id="btn-download-openapi" download="openapi.json">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path><polyline points="7 10 12 15 17 10"></polyline><line x1="12" y1="15" x2="12" y2="3"></line></svg>
                    Download Open API
                </a>
            </div>
        </header>

        <!-- Information Cards Grid -->
        <main class="grid">
            <!-- Tech Stack Card -->
            <section class="card">
                <h2>🛠️ Tech Stack</h2>
                <p>Built with modern enterprise standards on top of the latest Java runtime:</p>
                <div class="tech-list">
                    <span class="tech-badge"><strong>Java:</strong> 26</span>
                    <span class="tech-badge"><strong>Spring Boot:</strong> 4.1.1</span>
                    <span class="tech-badge"><strong>Spring MVC:</strong> Web</span>
                    <span class="tech-badge"><strong>Data & ORM:</strong> Hibernate / JPA</span>
                    <span class="tech-badge"><strong>Database:</strong> H2 In-Memory</span>
                    <span class="tech-badge"><strong>Documentation:</strong> SpringDoc OpenAPI 3</span>
                    <span class="tech-badge"><strong>Template:</strong> Apache FreeMarker</span>
                    <span class="tech-badge"><strong>Boilerplate:</strong> Project Lombok</span>
                    <span class="tech-badge"><strong>Build System:</strong> Gradle (Kotlin DSL)</span>
                </div>
                <table class="meta-table" style="margin-top: 1.25rem;">
                    <tr>
                        <th>Java Runtime</th>
                        <td>${javaVersion}</td>
                    </tr>
                    <tr>
                        <th>Version</th>
                        <td>${appVersion}</td>
                    </tr>
                    <tr>
                        <th>Datasource URL</th>
                        <td><code>${databaseUrl}</code></td>
                    </tr>
                </table>
            </section>

            <!-- Architecture & Namespaces Card -->
            <section class="card">
                <h2>🏛️ Architecture & Namespaces</h2>
                <p>Standardized package hierarchy for predictable separation of concerns:</p>
                <ul>
                    <li><code>config</code> - Configuration classes for Spring Boot</li>
                    <li><code>controller/rest</code> - RESTful API controllers (JSON endpoints)</li>
                    <li><code>controller/web</code> - Web MVC controllers (FreeMarker templates)</li>
                    <li><code>service</code> - Business logic and database initialization</li>
                    <li><code>model</code> - Hibernate JPA entities (automotive domain)</li>
                    <li><code>dao</code> - Spring Data JPA repositories</li>
                    <li><code>dto</code> - Data transfer and serialization objects</li>
                </ul>
            </section>

            <!-- Usage & Commands Card -->
            <section class="card">
                <h2>🚀 Usage & Quick Start</h2>
                <p>Common development commands and tasks:</p>
                <p><strong>Run Application:</strong></p>
                <pre><code>./gradlew bootRun</code></pre>
                <p><strong>Run Tests:</strong></p>
                <pre><code>./gradlew test</code></pre>
                <p><strong>H2 Database Console:</strong></p>
                <ul>
                    <li>Web Console: <a href="/h2" target="_blank"><code>/h2</code></a></li>
                    <li>JDBC URL: <code>jdbc:h2:mem:smartrestdb</code></li>
                    <li>Username: <code>sa</code> | Password: <code>password</code></li>
                </ul>
            </section>

            <!-- Endpoints Overview Card -->
            <section class="card">
                <h2>📡 REST Endpoints</h2>
                <p>Sample pre-loaded automotive domain endpoints:</p>
                <ul>
                    <li><code>GET /api/brands</code> - Brands catalogue</li>
                    <li><code>GET /api/dealers</code> - Dealership network</li>
                    <li><code>GET /api/cars</code> - Vehicles with specs (filters: brandId, dealerId, fuelType)</li>
                    <li><code>GET /api/alive</code> - Lightweight status & version check</li>
                    <li><code>GET /api/readiness</code> - Probes readiness status</li>
                </ul>
                <p style="margin-top: 0.5rem; color: var(--text-muted); font-size: 0.9rem;">
                    For interactive testing and schema details, click <strong>Explore APIs</strong> above.
                </p>
            </section>
        </main>

        <!-- Footer -->
        <footer>
            <p>
                ${appName} Java Edition &bull; Version ${appVersion} &bull; Licensed under ISC &bull;
                <a href="https://github.com/guildenstern70/SmartREST" target="_blank" rel="noopener noreferrer">SmartREST Kotlin Edition</a>
            </p>
        </footer>
    </div>
</body>
</html>

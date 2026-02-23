# Audit App Client

Nuxt application for visualizing results of code audit reports.

Provides dashboards for code quality metrics and trends in 3 different scopes:
- Company (all projects)
- Project (all repositories)
- Repository

Supports following tools (focusing on PHP in current version):
- SonarQube
- Semgrep
- PHPCS
- PHPStan

## Setup

Make sure to install dependencies

Using Bun is recommended for development for performance reasons and compatibility with Docker

### Install dependencies

```bash
# bun
bun install

# npm
npm install
```

## Development Server (standalone)

Provide environment variables

```bash
cp env/.env.development .env
```

Start the development server on `http://localhost:3000`:

```bash
# bun
bun dev

# npm
npm run dev
```

## Production

Build the application for production:

```bash
# bun
bun build

# npm
npm run build
```

Locally preview production build:

```bash
# bun
bun preview

# npm
npm run preview
```

Check out the [deployment documentation](https://nuxt.com/docs/getting-started/deployment) for more information.

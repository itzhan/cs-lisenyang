const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');

const repoRoot = path.resolve(__dirname, '..', '..');
const gitDir = path.join(repoRoot, '.git');
if (!fs.existsSync(gitDir)) {
  process.exit(0);
}

const huskyBin = path.resolve(
  __dirname,
  '..',
  'node_modules',
  '.bin',
  process.platform === 'win32' ? 'husky.cmd' : 'husky'
);
if (!fs.existsSync(huskyBin)) {
  process.exit(0);
}

const result = spawnSync(huskyBin, ['install'], {
  cwd: repoRoot,
  stdio: 'inherit',
});

process.exit(result.status ?? 0);

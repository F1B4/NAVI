import { defineConfig } from 'vite';
import eslintPlugin from '@nabla/vite-plugin-eslint';
import react from '@vitejs/plugin-react-swc';
import path from 'path';

export default defineConfig({
  plugins: [react(), eslintPlugin()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  // server: {
  //   port: 5173,
  //   strictPort: false,
  //   hmr: {
  //     port: 5173,
  //   },
  // },
  server: {
    port: 5173, // 포트 설정
    open: true, // 브라우저 자동 열기
    strictPort: false,
    proxy: {
      // 프록시 설정
      '/ws': {
        target: 'http://j10d107.p.ssafy.io:5173', // WebSocket을 사용하는 경우의 타겟 설정
        ws: true,
      },
    },
    hmr: {
      port: 5173,
    },
    cors: true, // CORS 허용
  },
});

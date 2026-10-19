/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#1E3A5F',
          light: '#2D5FA6',
        },
        accent: '#F59E0B',
        success: '#10B981',
        warning: '#F97316',
        danger: '#EF4444',
        surface: '#F8FAFC',
        card: '#FFFFFF',
        border: '#E2E8F0',
        textMain: '#0F172A',
        textMuted: '#64748B',
        dark: {
          surface: '#0F172A',
          card: '#1E293B',
          border: '#334155',
          textMain: '#F1F5F9',
          textMuted: '#94A3B8',
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
        heading: ['Sora', 'Inter', 'system-ui', 'sans-serif'],
      },
      width: {
        'sidebar': '240px',
      },
      height: {
        'header': '64px',
        'tabbar': '64px',
      },
      maxWidth: {
        'content': '1200px',
      }
    },
  },
  plugins: [],
}

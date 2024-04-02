import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface PlayState {
  type: string;
  playing: boolean;
  url: string;
  title: string;
  coverImage: string;
  artist: string;
  goPlay: (props: PlayProps) => void;
}

interface PlayProps {
  type: string;
  url: string;
  title: string;
  coverImage: string;
  artist?: string;
}

const usePlayStore = create(
  persist<PlayState>(
    (set) => ({
      type: '',
      playing: false,
      url: '',
      title: '',
      coverImage: '',
      artist: '',
      goPlay: (props: PlayProps) => {
        set((state) => {
          const newState = {
            ...state,
            playing: true,
            type: props.type,
            url: props.url,
            title: props.title,
            coverImage: props.coverImage,
            artist: props.artist,
          };
          console.log('Updated state:', newState);
          return newState;
        });
      },
    }),
    {
      name: 'playStrage',
    },
  ),
);

export { usePlayStore };
export type { PlayState, PlayProps };

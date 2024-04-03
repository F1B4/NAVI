import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface PlayState {
  pk: number;
  type: string;
  url: string;
  title: string;
  coverImage: string;
  artist: string;
  goPlay: (props: PlayProps) => void;
  expand: () => void;
}

interface PlayProps {
  pk: number;
  type: string;
  url: string;
  title: string;
  coverImage: string;
  expanded: boolean;
  artist?: string;
}

const usePlayStore = create(
  persist<PlayState>(
    (set) => ({
      pk: 0,
      type: '',
      url: '',
      title: '',
      coverImage: '',
      expanded: false,
      artist: '',
      goPlay: (props: PlayProps) => {
        set((state) => {
          const newState = {
            ...state,
            pk: props.pk,
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
      expand: () => {
        set((state) => {
          const newState = {
            ...state,
            expanded: false,
          };
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

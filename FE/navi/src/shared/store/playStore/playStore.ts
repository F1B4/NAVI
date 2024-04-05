import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface PlayState {
  play: boolean;
  expanded: boolean;
  pk: number;
  type: string;
  url: string;
  title: string;
  coverImage: string;
  artist: string;
  goPlay: (props: PlayProps) => void;
  expand: () => void;
  playToggle: () => void;
}

interface PlayProps {
  play: boolean;
  pk: number;
  expanded: boolean;
  type: string;
  url: string;
  title: string;
  coverImage: string;
  artist?: string;
}

const usePlayStore = create(
  persist<PlayState>(
    (set) => ({
      play: true,
      pk: 0,
      type: '',
      expanded: false,
      url: '',
      title: '',
      coverImage: '',
      artist: '',
      goPlay: (props: PlayProps) => {
        set((state) => {
          const newState = {
            ...state,
            play: true,
            expanded: false,

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
      playToggle: () => {
        set((state) => {
          const newState = {
            ...state,
            play: !state.play,
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

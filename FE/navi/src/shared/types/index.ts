interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
}

interface SongInfo {
  songPk: number;
  title: string;
  image: string;
  users: string;
}

interface CoverUser {
  id: number;
  userDto: User;
  partDto: {
    id: number;
    image: string;
    name: string;
  };
}

interface CoverList {
  resultCode: string;
  message: string;
  data: {
    id: number;
    title: string;
    thumbnail: string;
    hit: number;
    likeCount: number;
    songDto: SongInfo;
    createdAt: string;
    coverUserDtos: CoverUser[];
    likeExsits: boolean;
  }[];
}

export type { User, CoverUser, CoverList, SongInfo };

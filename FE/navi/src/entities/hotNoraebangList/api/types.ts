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
  mr: string;
  image: string;
}

interface NoraebangItem {
  id: number;
  content: string;
  record: string;
  hit: number;
  likeCount: number;
  songDto: SongInfo;
  userDto: User;
  createdAt: string;
  likeExsits: boolean;
}

interface NoraebangList {
  data: NoraebangItem[];
}

interface Response {
  resultCode: string;
  message: string;
  data: NoraebangList;
}

export type { Response, NoraebangList };

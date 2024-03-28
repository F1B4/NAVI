interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
}

interface SongInfo {
  id: number;
  title: string;
  mr: string;
  image: string;
}

interface NoraebangItem {
  id: number;
  record: string;
  hit: number;
  likeCount: number;
  songDto: SongInfo;
  userDto: User;
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

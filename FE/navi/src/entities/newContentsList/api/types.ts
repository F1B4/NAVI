interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
}

interface Part {
  id: number;
  image: string;
  name: string;
}

interface SongInfo {
  songPk: number;
  title: string;
  mr: string;
  image: string;
}

interface NewContent {
  id: number;
  content: string;
  record: string;
  hit: number;
  likeCount: number;
  songDto: SongInfo;
  coverUserDtos?: CoverUser;
  userDto?: User;
  createdAt: string;
  likeExsits: boolean;
}

interface CoverUser {
  id: number;
  userDto: User;
  partDto: Part;
}

interface NewContentsList {
  data: NewContent[];
}

interface Response {
  resultCode: string;
  message: string;
  data: NewContentsList;
}

export type { Response, NewContentsList, CoverUser };

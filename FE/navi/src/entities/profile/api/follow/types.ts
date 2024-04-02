interface User {
  id: number;
  nickname: string;
  email: string;
  image: string;
  followingCount: number;
  followerCount: number;
  username: string;
  role: string;
}

interface Follow {
  id: number;
  user: User;
}

interface Response {
  resultCode: string;
  message: string;
  data: Follow[];
}

export type { Response, Follow };

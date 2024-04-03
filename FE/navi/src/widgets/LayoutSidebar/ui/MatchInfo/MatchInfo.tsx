import { useEffect, useState } from 'react';
import axios from 'axios';
import { baseApi } from '@/shared/api';

interface DataForm {
  partCount: number;
  artists: number;
  title: string;
  image: string;
  groupName: string;
}

const SidebarComponent = () => {
  const [matchData, setMatchData] = useState<DataForm[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(`${baseApi}/covers/match`, {
          withCredentials: true,
        });
        setMatchData(response.data.data);
      } catch (error) {
        console.error('Error fetching match data:', error);
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      {matchData.map((item, index) => (
        <div
          key={index}
          className="radial-progress"
          style={{
            width: `${(item.partCount / item.artists) * 100}%`,
          }}
          role="progressbar"
        >
          {`${(item.partCount / item.artists) * 100}%`}
        </div>
      ))}
    </div>
  );
};

export default SidebarComponent;

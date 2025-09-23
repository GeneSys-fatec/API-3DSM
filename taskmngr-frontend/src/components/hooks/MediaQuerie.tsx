import { useState, useEffect } from "react";

/**
 * Hook customizado que verifica se a janela do browser corresponde a uma media query.
 * @param query A string da media query (ex: '(min-width: 1024px)')
 * @returns `true` se a query corresponder, `false` caso contrário.
 */

const useMediaQuery = (query: string): boolean => {
  const [matches, setMatches] = useState(
    () => window.matchMedia(query).matches
  );

  useEffect(() => {
    const mediaQueryList = window.matchMedia(query);

    const listener = (event: MediaQueryListEvent) => setMatches(event.matches);

    mediaQueryList.addEventListener("change", listener);

    return () => {
      mediaQueryList.removeEventListener("change", listener);
    };
  }, [query]);

  return matches;
};

export default useMediaQuery;

import { useNavigate } from "react-router-dom";
 
export function withNavigation(Component: any) {
    return function(props: any) {
        const navigate = useNavigate();
        return <Component {...props} navigate={navigate} />;
    }
}
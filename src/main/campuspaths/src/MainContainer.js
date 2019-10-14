import React, {Component} from 'react';
import Map from "./Map";
import PathSelector from "./PathSelector";

class MainContainer extends Component {
    constructor(props){
        super(props);
        this.state = {
            inputError: {origin:false, dest:false},
            origin: "Select Location",
            dest: "Select Location",
            path: []
        }
    }

    resetPage = () => {
        this.setState({
            inputError: {origin:false, dest:false},
            origin: "Select Location",
            dest: "Select Location",
            path: []});
}

    pathSelectorChange = (event) => {
        if(event.target.name === "originSelector"){
            this.setState({origin: event.target.value});
        }
        else{
            this.setState({dest: event.target.value});
        }

    }
    generatePath = () => {
        let newErrors = {origin: false, dest: false}
        if(this.state.dest === "Select Location"){
            newErrors.dest = true
        }
        if(this.state.origin === "Select Location"){
            newErrors.origin = true
        }
        this.setState({inputError:newErrors})
        if(newErrors.origin === true || newErrors.dest === true){
            alert("Please select valid locations")
        }
        else {
            fetch("http://localhost:4567/path?origin=" + this.state.origin + "&destination=" + this.state.dest)
                .then((res) => {
                    if (res.status !== 200) {
                        throw Error("Could Not Retrieve that path from " + this.state.origin + " to " + this.state.dest)
                    }
                    return res.json();
                }).then((pathData) => {
                    let newPath = [];
                    pathData.path.forEach((edge) => {
                        newPath.push({x1:edge.start.x,y1:edge.start.y,x2:edge.end.x,y2:edge.end.y})
                    })
                    this.setState({path:newPath})
                }).catch((error) => {
                    alert(error);
                })
        }
    }


    render() {
        return (
          <div>
              <h1>Campus Maps</h1>
              <PathSelector origin={this.state.origin} dest={this.state.dest}
                            onChange={this.pathSelectorChange} updatePath={this.generatePath}
                            error={this.state.inputError} clear={this.resetPage}/>
              <Map path={this.state.path} />
          </div>
        );

    }
}

export default MainContainer;
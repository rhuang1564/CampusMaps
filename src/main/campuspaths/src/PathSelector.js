import React, {Component} from 'react';
import {Button, InputLabel, MenuItem, Select} from "@material-ui/core";
import FormControl from "@material-ui/core/FormControl";

class PathSelector extends Component {

    constructor(props){
        super(props);
        this.state = {
            locations: this.generateDefaultLocation(),
        };
        this.generateLocations();
    }

    generateDefaultLocation = () => {
       return [(
        <MenuItem value="Select Location">
            Select Location
        </MenuItem>
       )]
    }
    generateLocations = () => {
        fetch("http://localhost:4567/buildings")
            .then((res) => {
                if(res.status !== 200) {
                    throw Error("Cannot retrieve locations");
                }
                return res.json();
            })
            .then((buildingMap) => {
                let locationItems = this.generateDefaultLocation();
                for(let shortName in buildingMap){
                    locationItems.push((
                            <MenuItem value={shortName}>
                                {buildingMap[shortName] + " (" + shortName + ")"}
                            </MenuItem>
                            )
                        )
                }
                this.setState({locations: locationItems});

            })
            .catch((error) => {
                alert(error);
            });
    };


    render() {
        return (
            <div>
                <FormControl error={this.props.error.origin}>
                    <InputLabel>Origin:</InputLabel>
                    <Select name={"originSelector"} value={this.props.origin} onChange={this.props.onChange}>
                        {this.state.locations}
                    </Select>
                </FormControl>
                <FormControl error={this.props.error.dest}>
                    <InputLabel>Destination</InputLabel>
                    <Select name={"destSelector"} value={this.props.dest} onChange={this.props.onChange}>
                        {this.state.locations}
                    </Select>
                </FormControl>
                <Button id="path-button" variant="contained" onClick={this.props.updatePath}>Find Path</Button>
                <Button id="clear-button" variant="contained" onClick={this.props.clear}>Clear</Button>
            </div>
        )
    }
}

export default PathSelector;
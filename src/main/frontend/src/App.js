import React, {Component} from 'react';
import Dropzone from 'react-dropzone'
import Request from 'superagent'
import './App.css';

class App extends Component {
    render() {
        return (
            <div className="App">
                <div className="App-header">
                    <h2>Welcome to Redirect Check!</h2>
                </div>
                <Basic/>

                <Tasks />



                <p className="App-intro">
                    To get started, edit <code>src/App.js</code> and save to reload.
                </p>
            </div>
        );
    }
}


class Tasks extends Component{
    render(){
        return(
            <table class="table">
                <thead>
                <tr>
                    <th>Source File</th>
                    <th>Status</th>
                    <th>Output</th>
                </tr>
                </thead>
                <tbody>

                {/*<tr th:each="task : ${tasks}">*/}
                    {/*<td th:text="${task.getInputFileName()}"></td>*/}
                    {/*<td th:text="${task.getStatus() + ' '+ task.getTaskProgress().getPerventageCompleted()} + '%'"></td>*/}
                    {/*<td><a th:href="${'/files/' + task.getOutputUri()}" th:text="${task.getOutputUri()}"/></td>*/}
                {/*</tr>*/}
                </tbody>
            </table>
        );
    }
}

class Basic extends Component {
    constructor() {
        super();
        this.state = {files: []}
    }

    onDrop(files) {
        this.setState({
            files
        });

        const req = Request.post('api/upload');
        files.forEach(file => {
            req.attach("file", file);
        });

        req.end(() => console.log("done"));
    }

    render() {
        return (
            <section>
                <div className="dropzone">
                    <Dropzone onDrop={this.onDrop.bind(this)}>
                        <p>Try dropping some files here, or click to select files to upload.</p>
                    </Dropzone>
                </div>
                <aside>
                    <h2>Dropped files</h2>
                    <ul>
                        {
                            this.state.files.map(f => <li>{f.name} - {f.size} bytes</li>)
                        }
                    </ul>
                </aside>
            </section>
        );
    }
}

//
// class UploadForm extends Component {
//     render() {
//         return (
//             <form className="form-group" method="POST" encType="multipart/form-data" action="/">
//                 <div className="form-group">
//                     <label>File to upload
//                         <input className="form-control-file" type="file" name="file" id="file"/>
//                     </label>
//                 </div>
//                 <button className="btn btn-primary" type="submit">Upload</button>
//             </form>
//         );
//     }
// }

export default App;

<template>
    <div>
        <v-container>
            <v-btn 
                elevation="3" 
                depressed 
                color="primary" 
                @click.native="regFile"
                class="d-inline-block float-right">
                파일 등록
            </v-btn>
            <v-btn 
                elevation="3" 
                depressed 
                color="primary" 
                @click.native="checkFile"
                class="d-inline-block float-right">
                파일 확인
            </v-btn>
            <v-file-input
                accept=".csv"
                v-model="selectFile"
                show-size
                truncate-length="15"
                class="float-right"
            ></v-file-input>
            <v-container class="grey lighten-5 mb-6 d-inline-block">
                <v-row class="text-center">
                    <v-col> 등록 날짜 </v-col>
                    <v-col> 파일 이름 </v-col>
                    <v-col> 상태 </v-col>
                    <v-col> 삭제 </v-col>
                </v-row>
                <v-row
                class="text-center"
                v-for="(item, index) in items"
                :key="index">
                    <v-col> {{ item.fileDate }} </v-col>
                    <v-col class="text-truncate"> <a href="#" @click="downloadFile(item)"> {{ item.fileName }} </a></v-col>
                    <v-col> {{ item.fileStatus }} </v-col>
                    <v-col> <a href="#" @click="deleteFile(item)">삭제</a> </v-col>
                </v-row>
            </v-container>
        </v-container>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
          items: [],
          selectFile: undefined,
          currentFile: undefined,
          progress: 0,
        };
    },
    methods: {
        checkFile() {
            console.log(this.selectFile);
        },
        regFile() {
            this.currentFile = this.selectFile;
            let formData = new FormData();
            formData.append("file", this.currentFile);
            axios ({
                method: "POST",
                url: this.$baseurl + '/main/file',
                headers: {
                    "Content-Type" : "multipart/form-data"
                },
                data: formData
            })
            .then((res) => {
                let stat = res.data.status;
                if(stat === false) {
                    alert("이미 존재하는 파일입니다.");
                } else {
                    alert("파일이 등록되었습니다.");
                }
                this.$router.go();
            })
            .catch((err) => {
                console.log(err);
                this.progress = 0;
                this.currentFile = undefined;
            })
            .finally(() => {
                this.selectFile = undefined;
            })
        },
        getFiles() {
            axios ({
                method: "GET",
                url: this.$baseurl + '/main/file'
            })
            .then((resp) => {
                this.items = resp.data.object;
            })
            .catch((err) => {
                console.log(err);
            })
        },
        downloadFile(aFile) {
            location.href=aFile.fileUri;
        },
        deleteFile(file) {
            if(file.fileStatus === 'TODO') {
                axios ({
                    method: "DELETE",
                    url: this.$baseurl + '/main/file/' + file.id,
                })
                .then(() => {
                    alert("파일을 삭제하였습니다.");
                })
                .catch(() => {
                    alert("파일을 삭제할 수 없습니다.");
                })
                .finally(() => {
                    this.$router.go();
                })
            } else {
                alert("분류중이거나 이미 완료된 파일은 삭제할 수 없습니다.")
            }
        }
    },
    mounted() {
        this.getFiles();
    },
}
</script>

<style>

</style>